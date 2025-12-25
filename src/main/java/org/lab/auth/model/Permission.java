package org.lab.auth.model;

public enum Permission {
    PROJECT_SET_TEAM_LEAD("project.setTeamLead", "Set team lead for a project"),
    PROJECT_ADD_DEVELOPER("project.addDeveloper", "Add developer to a project"),
    PROJECT_ADD_TESTER("project.addTester", "Add tester to a project"),
    PROJECT_TEST("project.test", "Test a project"),

    TICKET_CREATE("ticket.create", "Create a new ticket"),
    TICKET_ASSIGN_DEVELOPER("ticket.assignDeveloper", "Assign developer to a ticket"),
    TICKET_GET_STATUS("ticket.getStatus", "Get ticket status"),
    TICKET_COMPLETE("ticket.complete", "Complete a ticket"),

    BUG_REPORT_CREATE("bugReport.create", "Create a new bug report"),
    BUG_REPORT_FIX("bugReport.fix", "Mark bug report as fixed"),
    BUG_REPORT_TEST("bugReport.test", "Mark bug report as tested"),
    BUG_REPORT_CLOSE("bugReport.close", "Close a bug report"),

    MILESTONE_CREATE("milestone.create", "Create a new milestone"),
    MILESTONE_SET_STATUS("milestone.setStatus", "Set milestone status");

    private final String name;
    private final String description;

    Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

