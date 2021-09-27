package de.berlinerschachverband.bmm.basedata.controller;

import de.berlinerschachverband.bmm.basedata.data.ClubData;
import de.berlinerschachverband.bmm.basedata.data.thymeleaf.CreateClubData;
import de.berlinerschachverband.bmm.basedata.service.ClubService;
import de.berlinerschachverband.bmm.navigation.NavbarData;
import de.berlinerschachverband.bmm.navigation.NavbarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubController.class)
class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NavbarService navbarService;

    @MockBean
    private ClubService clubService;

    @BeforeEach
    private void setUp() {
        when(navbarService.getNavbarData()).thenReturn(new NavbarData(List.of("testSeason", "testSeason2")));
    }

    @Test
    void testGetClubs() throws Exception {
        when(clubService.getAllClubs())
                .thenReturn(List.of(
                        new ClubData(1L, "club1", true),
                        new ClubData(2L, "club2", false)));
        this.mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubs", List.of(
                        new ClubData(1L, "club1", true),
                        new ClubData(2L, "club2", false))));
    }

    @Test
    void testGetAllActiveClubs() throws Exception {
        when(clubService.getAllActiveClubs())
                .thenReturn(List.of(
                        new ClubData(1L, "club1", true),
                        new ClubData(2L, "club2", true)));
        this.mockMvc.perform(get("/clubs/active"))
                .andExpect(status().isOk())
                .andExpect(view().name("activeClubs"))
                .andExpect(model().attribute("navbarData", new NavbarData(List.of("testSeason", "testSeason2"))))
                .andExpect(model().attribute("clubs", List.of(
                        new ClubData(1L, "club1", true),
                        new ClubData(2L, "club2", true))));
    }

    @Test
    void testPostCreateClubSuccess() throws Exception {
        CreateClubData createClubData = new CreateClubData();
        createClubData.setClubName("club1");
        when(clubService.createClub("club1")).thenReturn(new ClubData(1L, "club1", true));

        this.mockMvc.perform(post("/club/create")
                .flashAttr("createClubData", createClubData))
                .andExpect(status().isOk())
                .andExpect(view().name("clubCreated"))
                .andExpect(model().attributeExists("navbarData"))
                .andExpect(model().attribute("state", "success"))
                .andExpect(model().attribute("club", new ClubData(1L, "club1", true)));

    }
}