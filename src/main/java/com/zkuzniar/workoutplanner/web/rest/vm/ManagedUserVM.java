package com.zkuzniar.workoutplanner.web.rest.vm;

import com.zkuzniar.workoutplanner.service.dto.AdminUserDTO;

public class ManagedUserVM extends AdminUserDTO {

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
