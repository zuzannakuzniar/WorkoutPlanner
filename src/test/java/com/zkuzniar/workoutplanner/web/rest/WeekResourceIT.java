package com.zkuzniar.workoutplanner.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zkuzniar.workoutplanner.IntegrationTest;
import com.zkuzniar.workoutplanner.domain.Week;
import com.zkuzniar.workoutplanner.repository.WeekRepository;
import com.zkuzniar.workoutplanner.service.dto.WeekDTO;
import com.zkuzniar.workoutplanner.service.mapper.WeekMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeekResourceIT {

    private static final Integer DEFAULT_WEEK_OF_YEAR = 1;
    private static final Integer UPDATED_WEEK_OF_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/weeks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeekRepository weekRepository;

    @Autowired
    private WeekMapper weekMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeekMockMvc;

    private Week week;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Week createEntity(EntityManager em) {
        Week week = new Week().weekOfYear(DEFAULT_WEEK_OF_YEAR);
        return week;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Week createUpdatedEntity(EntityManager em) {
        Week week = new Week().weekOfYear(UPDATED_WEEK_OF_YEAR);
        return week;
    }

    @BeforeEach
    public void initTest() {
        week = createEntity(em);
    }

    @Test
    @Transactional
    void createWeek() throws Exception {
        int databaseSizeBeforeCreate = weekRepository.findAll().size();
        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);
        restWeekMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeCreate + 1);
        Week testWeek = weekList.get(weekList.size() - 1);
        assertThat(testWeek.getWeekOfYear()).isEqualTo(DEFAULT_WEEK_OF_YEAR);
    }

    @Test
    @Transactional
    void createWeekWithExistingId() throws Exception {
        // Create the Week with an existing ID
        week.setId(1L);
        WeekDTO weekDTO = weekMapper.toDto(week);

        int databaseSizeBeforeCreate = weekRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeekMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeeks() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get all the weekList
        restWeekMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(week.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekOfYear").value(hasItem(DEFAULT_WEEK_OF_YEAR)));
    }

    @Test
    @Transactional
    void getWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get the week
        restWeekMockMvc
            .perform(get(ENTITY_API_URL_ID, week.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(week.getId().intValue()))
            .andExpect(jsonPath("$.weekOfYear").value(DEFAULT_WEEK_OF_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingWeek() throws Exception {
        // Get the week
        restWeekMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        int databaseSizeBeforeUpdate = weekRepository.findAll().size();

        // Update the week
        Week updatedWeek = weekRepository.findById(week.getId()).get();
        // Disconnect from session so that the updates on updatedWeek are not directly saved in db
        em.detach(updatedWeek);
        updatedWeek.weekOfYear(UPDATED_WEEK_OF_YEAR);
        WeekDTO weekDTO = weekMapper.toDto(updatedWeek);

        restWeekMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weekDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isOk());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
        Week testWeek = weekList.get(weekList.size() - 1);
        assertThat(testWeek.getWeekOfYear()).isEqualTo(UPDATED_WEEK_OF_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weekDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeekWithPatch() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        int databaseSizeBeforeUpdate = weekRepository.findAll().size();

        // Update the week using partial update
        Week partialUpdatedWeek = new Week();
        partialUpdatedWeek.setId(week.getId());

        restWeekMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeek.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeek))
            )
            .andExpect(status().isOk());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
        Week testWeek = weekList.get(weekList.size() - 1);
        assertThat(testWeek.getWeekOfYear()).isEqualTo(DEFAULT_WEEK_OF_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateWeekWithPatch() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        int databaseSizeBeforeUpdate = weekRepository.findAll().size();

        // Update the week using partial update
        Week partialUpdatedWeek = new Week();
        partialUpdatedWeek.setId(week.getId());

        partialUpdatedWeek.weekOfYear(UPDATED_WEEK_OF_YEAR);

        restWeekMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeek.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeek))
            )
            .andExpect(status().isOk());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
        Week testWeek = weekList.get(weekList.size() - 1);
        assertThat(testWeek.getWeekOfYear()).isEqualTo(UPDATED_WEEK_OF_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weekDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeek() throws Exception {
        int databaseSizeBeforeUpdate = weekRepository.findAll().size();
        week.setId(count.incrementAndGet());

        // Create the Week
        WeekDTO weekDTO = weekMapper.toDto(week);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeekMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weekDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Week in the database
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        int databaseSizeBeforeDelete = weekRepository.findAll().size();

        // Delete the week
        restWeekMockMvc
            .perform(delete(ENTITY_API_URL_ID, week.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Week> weekList = weekRepository.findAll();
        assertThat(weekList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
