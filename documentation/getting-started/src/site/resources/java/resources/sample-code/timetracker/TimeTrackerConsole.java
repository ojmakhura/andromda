package org.andromda.timetracker.console;

import java.util.Date;

import org.andromda.timetracker.ServiceLocator;
import org.andromda.timetracker.domain.TimecardStatus;
import org.andromda.timetracker.service.PeopleService;
import org.andromda.timetracker.service.TimeTrackingService;
import org.andromda.timetracker.vo.PersonVO;
import org.andromda.timetracker.vo.TaskVO;
import org.andromda.timetracker.vo.TimeAllocationVO;
import org.andromda.timetracker.vo.TimePeriodVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.andromda.timetracker.vo.TimecardVO;

public class TimeTrackerConsole {

    // ServiceLocator and Services
    private static ServiceLocator serviceLocator = null;
    private static PeopleService peopleService = null;
    private static TimeTrackingService timeTrackingService = null;

    // Constants
    private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    public static void main(String[] args) {
        // Get services
        serviceLocator = ServiceLocator.instance();
        peopleService = serviceLocator.getPeopleService();
        timeTrackingService = serviceLocator.getTimeTrackingService();

        // Create people
        PersonVO naresh = createPerson("nbhatia", "Naresh", "Bhatia");
        PersonVO louis = createPerson("lcoude", "Louis", "Coude");
        PersonVO john = createPerson("jsmith", "John", "Smith");

        // Create tasks
        TaskVO research = createTask("Research");
        TaskVO development = createTask("Development");

        // Create timecards
        TimecardVO timecard1 = createTimecard(naresh, john);
        TimecardVO timecard2 = createTimecard(naresh, john);
        TimecardVO timecard3 = createTimecard(louis, john);
        TimecardVO timecard4 = createTimecard(louis, john);

        // Fetch and show all objects created above
        PersonVO[] people = peopleService.getAllPeople();
        showPeople(people);
        TaskVO[] tasks = timeTrackingService.getAllTasks();
        showTasks(tasks);
        TimecardSummaryVO[] timecards = timeTrackingService.getAllTimecardSummaries();
        showTimecardSummaries(timecards);

        // Fetch and show timecard1 details
        System.out.println("Timecard " + timecard1.getId() + " Details:");
        TimecardVO timecard1FromDB = timeTrackingService.getTimecard(timecard1.getId());
        showTimecard(timecard1FromDB);
    }

    private static PersonVO createPerson(String username, String firstName, String lastName) {
        PersonVO person = new PersonVO(null, username, firstName, lastName);
        person.setId(peopleService.createPerson(person));
        System.out.println("Person " + person.getId() + " created - " + person.getUsername());
        return person;
    }

    private static TaskVO createTask(String taskName) {
        TaskVO task = new TaskVO(null, taskName);
        task.setId(timeTrackingService.createTask(task));
        System.out.println("Task " + task.getId() + " created - " + task.getName());
        return task;
    }

    private static TimecardVO createTimecard(PersonVO submitter, PersonVO approver) {
        // Instantiate a timecard
        TimecardVO timecard = new TimecardVO();
        timecard.setStatus(TimecardStatus.DRAFT);
        timecard.setBegDate(new Date());
        timecard.setSubmitterName(submitter.getUsername());
        timecard.setApproverName(approver.getUsername());
        timecard.setComments("On track!");

        // Get all tasks to choose from
        TaskVO[] tasks = timeTrackingService.getAllTasks();

        // Instantiate allocations
        int count = (int) (Math.random() * 3 + 1);
        TimeAllocationVO[] allocations = new TimeAllocationVO[count];
        timecard.setAllocations(allocations);
        for (int i = 0; i < count; i++) {
            Date startTime = new Date(timecard.getBegDate().getTime() + i * MILLIS_IN_DAY);
            Date endTime   = new Date(timecard.getBegDate().getTime() + (i + 1) * MILLIS_IN_DAY);
            TaskVO task = tasks[(int)Math.round((Math.random()*(tasks.length - 1)))];
            allocations[i] = new TimeAllocationVO(
                null,
                new TimePeriodVO(startTime, endTime),
                task.getId());
        }

        // Create timecard
        timecard.setId(timeTrackingService.createTimecard(timecard));

        System.out.println(
            "Timecard " + timecard.getId() + " created with " +
            timecard.getAllocations().length + " allocations");

        return timecard;
    }

    private static void showPeople(PersonVO[] people) {
        System.out.println("People:");
        for (int i=0; i<people.length; i++) {
            System.out.println(
                people[i].getId() + ": " + people[i].getUsername() + " - " +
                people[i].getFirstName() + " " + people[i].getLastName());
        }
        System.out.println();
    }

    private static void showTasks(TaskVO[] tasks) {
        System.out.println("Tasks:");
        for (int i=0; i<tasks.length; i++) {
            System.out.println(tasks[i].getId() + ": " + tasks[i].getName());
        }
        System.out.println();
    }

    private static void showTimecardSummaries(TimecardSummaryVO[] timecards) {
        System.out.println("Timecards:");
        for (int i=0; i<timecards.length; i++) {
            showTimecardSummary(timecards[i]);
        }
        System.out.println();
    }

    private static void showTimecardSummary(TimecardSummaryVO timecard) {
        System.out.println(
            timecard.getId() + ": status=" +
            timecard.getStatus() + ", begin date=" +
            timecard.getBegDate() + ", comments=" +
            timecard.getComments() + ", submitter=" +
            timecard.getSubmitterName() + ", approver=" +
            timecard.getApproverName());
    }

    private static void showTimecard(TimecardVO timecard) {
        showTimecardSummary(timecard);
        TimeAllocationVO[] allocations = timecard.getAllocations();
        for (int i=0; i<allocations.length; i++) {
            showTimeAllocation(allocations[i]);
        }
    }

    private static void showTimeAllocation(TimeAllocationVO allocation) {
        System.out.println(
            "    " +
            allocation.getId() + ": start time=" +
            allocation.getTimePeriodVO().getStartTime() + ", end time=" +
            allocation.getTimePeriodVO().getEndTime() + ", task id=" +
            allocation.getTaskId());
    }
}