package org.lab.model;

import java.util.UUID;

public sealed interface Entity permits BugReport, Milestone, Project, Ticket, User {
    UUID id();
}

