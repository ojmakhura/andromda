using System;
using System.Collections;
using NHibernate.Support;
using TimeTracker.Domain;

namespace TimeTracker
{
    public class Program
    {
        private static Random random;

        // Data Access Objects
        private static IPersonDao personDao = DaoFactory.GetPersonDao();
        private static ITaskDao taskDao = DaoFactory.GetTaskDao();
        private static ITimecardDao timecardDao = DaoFactory.GetTimecardDao();
        private static ITimeAllocationDao timeAllocationDao = DaoFactory.GetTimeAllocationDao();

        public static void Main(string[] args)
        {
            StartApplication();
            ProcessMessages();
            EndApplication();
        }

        private static void CreateDatabase()
        {
            Console.WriteLine();
            Console.WriteLine("WARNING: This action will drop and recreate your database.");
            Console.Write("Are you sure you want to do this? [y] or [n]: ");
            String inputString = Console.ReadLine();
            inputString = inputString.ToLower().Trim();
            if (!inputString.Equals("y"))
                return;

            Console.WriteLine("Creating database...");
            DbSupport.CreateDatabase(true, true);
            Console.WriteLine("Database created.");
        }

        private static void AddPerson()
        {
            Console.Write("Person Name: ");
            String name = Console.ReadLine();

            // Instantiate the person
            Person person = Person.Factory.newInstance();
            person.Name = name;
            person.Rate = Quantity.newInstance(10, "USD/hour");

            // Persist in database
            personDao.BeginTransaction();
            try
            {
                personDao.Create(person);
                personDao.CommitTransaction();
                Console.WriteLine(name + " added.");
            }
            catch (Exception e)
            {
                personDao.RollbackTransaction();
                Console.WriteLine(e);
            }
        }

        private static void AddTask()
        {
            Console.Write("Task Name: ");
            String name = Console.ReadLine();

            // Instantiate the task
            Task task = Task.Factory.newInstance();
            task.Name = name;

            // Persist in database
            taskDao.BeginTransaction();
            try
            {
                taskDao.Create(task);
                taskDao.CommitTransaction();
                Console.WriteLine("Task " + task.Id + " added: " + task.Name);
            }
            catch (Exception e)
            {
                taskDao.RollbackTransaction();
                Console.WriteLine(e);
            }
        }

        private static void AddTimecard()
        {
            Console.WriteLine("Adding timecard...");

            timecardDao.BeginTransaction();
            try
            {
                // Pick a person who is submitting the timecard
                IList people = new ArrayList(personDao.LoadAll());
                Person person = (Person)people[random.Next(people.Count)];

                // Instantiate a timecard and persist it
                Timecard timecard = Timecard.Factory.newInstance();
                timecard.BegDate = DateTime.Now;
                timecard.Status = TimecardStatus.Draft;
                person.AddTimecard(timecard);
                timecardDao.Create(timecard);

                // Instantiate allocations and persist them
                IList tasks = new ArrayList(taskDao.LoadAll());
                int count = random.Next(1, 4);
                for (int i = 0; i < count; i++)
                {
                    TimeAllocation timeAllocation = TimeAllocation.Factory.newInstance();
                    timeAllocation.BegTime = timecard.BegDate.Value.Add(new TimeSpan(i, 0, 0));
                    timeAllocation.EndTime = timecard.BegDate.Value.Add(new TimeSpan(i + 1, 0, 0));
                    Task task = (Task)tasks[random.Next(tasks.Count)];
                    task.AddTimeAllocation(timeAllocation);
                    timecard.AddTimeAllocation(timeAllocation);
                    timeAllocationDao.Create(timecard.Allocations);
                }

                // Commit the transaction
                timecardDao.CommitTransaction();

                Console.WriteLine(
                    "Timecard " + timecard.Id + " created with " +
                    timecard.Allocations.Count + " allocations");
            }
            catch (Exception e)
            {
                timecardDao.RollbackTransaction();
                Console.WriteLine(e);
            }
        }

        private static void UpdateTimecard()
        {
            // Choose a timecard to update
            int id = 0;
            Console.Write("Timecard Id (enter 0 to skip): ");
            try
            {
                id = Convert.ToInt32(Console.ReadLine());
                if (id == 0) return;
            }
            catch (Exception)
            {
                Console.WriteLine("Invalid number");
                return;
            }

            // Update the timecard
            timecardDao.BeginTransaction();
            try
            {
                // Get timecard from database
                Timecard timecard = timecardDao.Load(id);
                Console.WriteLine("Current value:");
                ShowTimecard(timecard);

                // Modify timecard values
                timecard.BegDate = timecard.BegDate.Value.Add(new TimeSpan(1, 0, 0)); ;

                // Modify time allocation values
                foreach (TimeAllocation timeAllocation in timecard.Allocations)
                {
                    timeAllocation.BegTime = timeAllocation.BegTime.Value.Add(new TimeSpan(1, 0, 0));
                    timeAllocation.EndTime = timeAllocation.EndTime.Value.Add(new TimeSpan(1, 0, 0));
                }

                // Update timecard in database. Allocations updates should cascade.
                timecardDao.Update(timecard);
                timecardDao.CommitTransaction();

                Console.WriteLine("Timecard updated");
            }
            catch (Exception e)
            {
                timecardDao.RollbackTransaction();
                Console.WriteLine(e);
            }
        }

        private static void DeleteTimecard()
        {
            // Choose a timecard to delete
            int id = 0;
            Console.Write("Timecard Id (enter 0 to skip): ");
            try
            {
                id = Convert.ToInt32(Console.ReadLine());
                if (id == 0) return;
            }
            catch (Exception)
            {
                Console.WriteLine("Invalid number");
                return;
            }

            // Delete the timecard
            timecardDao.BeginTransaction();
            try
            {
                timecardDao.Remove(id);
                timecardDao.CommitTransaction();
                Console.WriteLine("Timecard removed");
            }
            catch (Exception e)
            {
                timecardDao.RollbackTransaction();
                Console.WriteLine(e);
            }
        }

        private static void ShowData()
        {
            ShowPeople();
            Console.WriteLine();
            ShowTasks();
            Console.WriteLine();
            ShowTimecards();
        }

        private static void ShowPeople()
        {
            Console.WriteLine("People:");
            ICollection people = personDao.LoadAll();
            foreach (Person person in people)
            {
                Console.WriteLine(
                    person.Id +
                    ": " + person.Name +
                    ", rate=" + person.Rate.Value +
                    " " + person.Rate.Unit);
            }
        }

        private static void ShowTasks()
        {
            Console.WriteLine("Tasks:");
            ICollection tasks = taskDao.LoadAll();
            foreach (Task task in tasks)
            {
                Console.WriteLine(
                    task.Id +
                    ": " + task.Name);
            }
        }

        private static void ShowTimecards()
        {
            Console.WriteLine("Timecards:");
            ICollection timecards = timecardDao.LoadAll();
            foreach (Timecard timecard in timecards)
            {
                ShowTimecard(timecard);
            }
        }

        private static void ShowTimecard(Timecard timecard)
        {
            Console.WriteLine(
                timecard.Id +
                ": " + timecard.Owner.Name +
                ", " + timecard.Status +
                ", " + timecard.BegDate);
            foreach (TimeAllocation timeAllocation in timecard.Allocations)
            {
                Console.WriteLine(
                    "    " + timeAllocation.Id +
                    ": " + timeAllocation.BegTime +
                    ", " + timeAllocation.EndTime +
                    ", " + timeAllocation.Task.Name);
            }
        }

        // ----- Helper Methods -----
        enum MenuChoice
        {
            None,
            CreateDatabase,
            AddPerson,
            AddTask,
            AddTimecard,
            UpdateTimecard,
            DeleteTimecard,
            ShowData,
            Quit,
            ShowMenu,
            Last
        }

        private static void StartApplication()
        {
            // Initialize the random number generator
            System.Threading.Thread.Sleep(1); // wait to allow the timer to advance
            random = new Random();

            // Initialize NHibernate
            SessionManagerFactory.SessionManager = new ThreadLocalSessionManager();
            SessionManagerFactory.SessionManager.HandleApplicationStart();
        }

        private static void ProcessMessages()
        {
            Console.WriteLine();
            ShowMenu();

            MenuChoice menuChoice = MenuChoice.None;
            while (menuChoice != MenuChoice.Quit)
            {
                menuChoice = GetMenuChoice();

                // Process the user's choice
                SessionManagerFactory.SessionManager.HandleSessionStart();
                try
                {
                    switch (menuChoice)
                    {
                        case MenuChoice.None:
                            break;
                        case MenuChoice.CreateDatabase:
                            CreateDatabase();
                            break;
                        case MenuChoice.AddPerson:
                            AddPerson();
                            break;
                        case MenuChoice.AddTask:
                            AddTask();
                            break;
                        case MenuChoice.AddTimecard:
                            AddTimecard();
                            break;
                        case MenuChoice.UpdateTimecard:
                            UpdateTimecard();
                            break;
                        case MenuChoice.DeleteTimecard:
                            DeleteTimecard();
                            break;
                        case MenuChoice.ShowData:
                            ShowData();
                            break;
                        case MenuChoice.ShowMenu:
                            ShowMenu();
                            break;
                        case MenuChoice.Quit:
                            break;
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
                finally
                {
                    SessionManagerFactory.SessionManager.HandleSessionEnd();
                }
            }
        }

        private static void EndApplication()
        {
            SessionManagerFactory.SessionManager.HandleApplicationEnd();
        }

        private static void ShowMenu()
        {
            Console.WriteLine("1. Create Database");
            Console.WriteLine("2. Add Person");
            Console.WriteLine("3. Add Task");
            Console.WriteLine("4. Add Timecard");
            Console.WriteLine("5. Update Timecard");
            Console.WriteLine("6. Delete Timecard");
            Console.WriteLine("7. Show Data");
        }

        private static MenuChoice GetMenuChoice()
        {
            Console.WriteLine();
            Console.Write("Please make a choice ([h]elp [q]uit): ");

            MenuChoice menuChoice = MenuChoice.None;
            String inputString = Console.ReadLine();
            inputString = inputString.ToLower().Trim();
            try
            {
                if (inputString.Equals("h"))
                {
                    menuChoice = MenuChoice.ShowMenu;
                }
                else if (inputString.Equals("q"))
                {
                    menuChoice = MenuChoice.Quit;
                }
                else
                {
                    int intChoice = Convert.ToInt32(inputString);
                    if (intChoice > (int)MenuChoice.None && intChoice < (int)MenuChoice.Last)
                        menuChoice = (MenuChoice)intChoice;
                }
            }
            catch (Exception) { }
            if (menuChoice == MenuChoice.None)
            {
                Console.WriteLine("Invalid choice!!!");
                menuChoice = MenuChoice.ShowMenu;
            }
            Console.WriteLine();
            return menuChoice;
        }
    }
}