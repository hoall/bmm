package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.basedata.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private final TeamRepository teamRepository = mock(TeamRepository.class);
    private final DivisionService divisionService = mock(DivisionService.class);
    private final ClubService clubService = mock(ClubService.class);
    private TeamService teamService;
    private SeasonData season1;
    private Season season;
    private Division division;
    private Club club1, club2;
    private Team team1, team2, team3;

    @BeforeEach
    private void setUp() {
        teamService = new TeamService(teamRepository, divisionService, clubService);
        season1 = new SeasonData(1L, "season1");
        season = new Season();
        season.setName("season1");
        division = new Division();
        division.setId(1L);
        division.setName("division1");
        division.setLevel(1);
        division.setSeason(season);
        club1 = new Club();
        club1.setId(1L);
        club1.setName("club1");
        club2 = new Club();
        club2.setName("club2");
        team1 = new Team();
        team1.setId(1L);
        team1.setClub(club1);
        team1.setDivision(division);
        team1.setNumber(1);
        team2 = new Team();
        team2.setId(2L);
        team2.setClub(club2);
        team2.setDivision(division);
        team2.setNumber(1);
        team3 = new Team();
        team3.setId(3L);
        team3.setClub(club1);
        team3.setNumber(2);
    }

    @Test
    void testGetTeamsOfDivision() {
        Set<TeamData> expected = Set.of(
                new TeamData(1L,
                        new ClubData(1L, "club1", true),
                        Optional.of(new DivisionData(1L, "division1", 1, season1)),
                        1),
                new TeamData(2L,
                        new ClubData(2L, "club2", true),
                        Optional.of(new DivisionData(1L, "division1", 1, season1)),
                        1)
        );
        when(teamRepository.findByDivision_Id(1L)).thenReturn(Set.of(team1, team2));
        when(clubService.toClubData(club1)).thenReturn(new ClubData(1L, "club1", true));
        when(clubService.toClubData(club2)).thenReturn(new ClubData(2L, "club2", true));
        when(divisionService.toDivisionData(division)).thenReturn(new DivisionData(1L, "division1", 1, season1));

        assertEquals(expected, teamService.getTeamsOfDivision(new DivisionData(1L, "division1", 1, season1)));
    }

    @Test
    void testGetTeamsOfClub() {
        when(clubService.toClubData(club1)).thenReturn(new ClubData(1L, "club1", true));
        when(teamRepository.findByClub_NameAndDivisionIsNull("club1")).thenReturn(Set.of(team1, team3));
        when(divisionService.toDivisionData(division)).thenReturn(new DivisionData(1L, "division1", 1, season1));

        assertEquals(List.of(
                new TeamData(1L,
                        new ClubData(1L, "club1", true),
                        Optional.of(new DivisionData(1L, "division1", 1, season1)),
                        1),
                new TeamData(3L,
                        new ClubData(1L, "club1", true),
                        Optional.empty(),
                        2)
                ),
                teamService.getTeamsOfClub("club1"));
    }
    
}