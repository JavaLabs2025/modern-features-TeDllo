package org.lab;

import org.junit.jupiter.api.BeforeEach;
import org.lab.auth.AuthRepository;
import org.lab.auth.AuthService;
import org.lab.auth.AuthServiceImpl;
import org.lab.auth.AuthenticationContext;
import org.lab.auth.InMemoryAuthRepository;
import org.lab.repository.*;
import org.lab.repository.inmemory.*;
import org.lab.service.*;

import java.util.UUID;

public abstract class TestBase {
    protected UserRepository userRepository;
    protected ProjectRepository projectRepository;
    protected TicketRepository ticketRepository;
    protected MilestoneRepository milestoneRepository;
    protected BugReportRepository bugReportRepository;
    protected AuthRepository authRepository;

    protected UserService userService;
    protected ProjectService projectService;
    protected TicketService ticketService;
    protected MilestoneService milestoneService;
    protected BugReportService bugReportService;
    protected AuthService authService;

    protected UUID managerId;
    protected UUID teamLeadId;
    protected UUID developerId;
    protected UUID testerId;

    @BeforeEach
    protected void baseSetUp() {
        userRepository = new InMemoryUserRepository();
        projectRepository = new InMemoryProjectRepository();
        ticketRepository = new InMemoryTicketRepository();
        milestoneRepository = new InMemoryMilestoneRepository();
        bugReportRepository = new InMemoryBugReportRepository();
        authRepository = new InMemoryAuthRepository();

        authService = new AuthServiceImpl(authRepository);
        userService = new UserService(userRepository);
        projectService = new ProjectService(projectRepository, authService);
        ticketService = new TicketService(ticketRepository, authService);
        milestoneService = new MilestoneService(milestoneRepository, ticketRepository, authService);
        bugReportService = new BugReportService(bugReportRepository, authService);

        managerId = userService.register("Manager").id();
        teamLeadId = userService.register("TeamLead").id();
        developerId = userService.register("Developer").id();
        testerId = userService.register("Tester").id();
    }

    protected void setCurrentUser(UUID userId) {
        AuthenticationContext.set(userId);
    }

    protected void clearCurrentUser() {
        AuthenticationContext.clear();
    }
}

