package org.lab.auth.model;

import java.util.List;

public enum Role {
    MANAGER("manager", List.of(
        Permission.PROJECT_SET_TEAM_LEAD.getName(),
        Permission.PROJECT_ADD_DEVELOPER.getName(),
        Permission.PROJECT_ADD_TESTER.getName(),
        Permission.TICKET_CREATE.getName(),
        Permission.TICKET_ASSIGN_DEVELOPER.getName(),
        Permission.TICKET_GET_STATUS.getName(),
        Permission.MILESTONE_CREATE.getName(),
        Permission.MILESTONE_SET_STATUS.getName()
    )),

    DEVELOPER("developer", List.of(
        Permission.TICKET_COMPLETE.getName(),
        Permission.BUG_REPORT_CREATE.getName(),
        Permission.BUG_REPORT_FIX.getName(),
        Permission.BUG_REPORT_CLOSE.getName()
    )),

    TESTER("tester", List.of(
        Permission.PROJECT_TEST.getName(),
        Permission.BUG_REPORT_CREATE.getName(),
        Permission.BUG_REPORT_TEST.getName(),
        Permission.BUG_REPORT_CLOSE.getName()
    )),

    TEAM_LEAD("teamLead", List.of(
        Permission.TICKET_CREATE.getName(),
        Permission.TICKET_ASSIGN_DEVELOPER.getName(),
        Permission.TICKET_GET_STATUS.getName(),
        Permission.TICKET_COMPLETE.getName()
    ));

    private final String name;
    private final List<String> permissions;

    Role(String name, List<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}

