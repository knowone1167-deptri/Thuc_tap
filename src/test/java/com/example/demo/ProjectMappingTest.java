package com.example.demo;

import org.junit.jupiter.api.Test;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
class ProjectMappingTest {

    @Test
    @DisplayName("Project phải map đúng với Owner")
    void shouldMapProjectToOwnerCorrectly() {
        User owner = new User("admin", "admin@test.com", "123");
        Project project = new Project(
                "Demo Project",
                "Description",
                owner
        );
        assertAll("Verify project-owner mapping",
                () -> assertNotNull(project.getOwner(), "Owner không được null"),
                () -> assertSame(owner, project.getOwner(), "Owner phải đúng object truyền vào")
        );
    }
}
