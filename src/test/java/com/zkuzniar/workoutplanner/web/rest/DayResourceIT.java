package com.zkuzniar.workoutplanner.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zkuzniar.workoutplanner.IntegrationTest;
import com.zkuzniar.workoutplanner.domain.Day;
import com.zkuzniar.workoutplanner.repository.DayRepository;
import com.zkuzniar.workoutplanner.service.dto.DayDTO;
import com.zkuzniar.workoutplanner.service.mapper.DayMapper;
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
class DayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_WEEK_ID = 1L;
    private static final Long UPDATED_WEEK_ID = 2L;

    private static final String ENTITY_API_URL = "/api/days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private DayMapper dayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDayMockMvc;

    private Day day;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createEntity(EntityManager em) {
        Day day = new Day().name(DEFAULT_NAME).weekId(DEFAULT_WEEK_ID);
        return day;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Day createUpdatedEntity(EntityManager em) {
        Day day = new Day().name(UPDATED_NAME).weekId(UPDATED_WEEK_ID);
        return day;
    }

    @BeforeEach
    public void initTest() {
        day = createEntity(em);
    }

    @Test
    @Transactional
    void createDay() throws Exception {
        int databaseSizeBeforeCreate = dayRepository.findAll().size();
        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);
        restDayMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate + 1);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDay.getWeekId()).isEqualTo(DEFAULT_WEEK_ID);
    }

    @Test
    @Transactional
    void createDayWithExistingId() throws Exception {
        // Create the Day with an existing ID
        day.setId(1L);
        DayDTO dayDTO = dayMapper.toDto(day);

        int databaseSizeBeforeCreate = dayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDays() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get all the dayList
        restDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(day.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weekId").value(hasItem(DEFAULT_WEEK_ID.intValue())));
    }

    @Test
    @Transactional
    void getDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        // Get the day
        restDayMockMvc
            .perform(get(ENTITY_API_URL_ID, day.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(day.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weekId").value(DEFAULT_WEEK_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDay() throws Exception {
        // Get the day
        restDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day
        Day updatedDay = dayRepository.findById(day.getId()).get();
        // Disconnect from session so that the updates on updatedDay are not directly saved in db
        em.detach(updatedDay);
        updatedDay.name(UPDATED_NAME).weekId(UPDATED_WEEK_ID);
        DayDTO dayDTO = dayMapper.toDto(updatedDay);

        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDay.getWeekId()).isEqualTo(UPDATED_WEEK_ID);
    }

    @Test
    @Transactional
    void putNonExistingDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDayWithPatch() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day using partial update
        Day partialUpdatedDay = new Day();
        partialUpdatedDay.setId(day.getId());

        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDay.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDay))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDay.getWeekId()).isEqualTo(DEFAULT_WEEK_ID);
    }

    @Test
    @Transactional
    void fullUpdateDayWithPatch() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeUpdate = dayRepository.findAll().size();

        // Update the day using partial update
        Day partialUpdatedDay = new Day();
        partialUpdatedDay.setId(day.getId());

        partialUpdatedDay.name(UPDATED_NAME).weekId(UPDATED_WEEK_ID);

        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDay.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDay))
            )
            .andExpect(status().isOk());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
        Day testDay = dayList.get(dayList.size() - 1);
        assertThat(testDay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDay.getWeekId()).isEqualTo(UPDATED_WEEK_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dayDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDay() throws Exception {
        int databaseSizeBeforeUpdate = dayRepository.findAll().size();
        day.setId(count.incrementAndGet());

        // Create the Day
        DayDTO dayDTO = dayMapper.toDto(day);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Day in the database
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDay() throws Exception {
        // Initialize the database
        dayRepository.saveAndFlush(day);

        int databaseSizeBeforeDelete = dayRepository.findAll().size();

        // Delete the day
        restDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, day.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Day> dayList = dayRepository.findAll();
        assertThat(dayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
