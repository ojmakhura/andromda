using System;
using System.Collections;
using NHibernate.Support;
using TimeTracker.Domain;

namespace TimeTracker
{
    public class Program
    {
        private static Random random;

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

            Person person = Person.Factory.newInstance();
            person.Name = name;
            person.Rate = Quantity.newInstance(10, "USD/hour");
            GetPersonDao().Create(person);
            Console.WriteLine(name + " added.");
        }

        private static void AddTask()
        {
            Console.Write("Task Name: ");
            String name = Console.ReadLine();

            Task task = Task.Factory.newInstance();
            task.Name = name;
            GetTaskDao().Create(task);
            Console.WriteLine("Task " + task.Id + " added: " + task.Name);
        }

        private static void AddTimecard()
        {
            Console.WriteLine("Adding timecard...");

            // Pick a person who is submitting the timecard
            IList people = new ArrayList(GetPersonDao().LoadAll());
            Person person = (Person)people[random.Next(people.Count)];

            // Create a timecard
            Timecard timecard = Timecard.Factory.newInstance();
            timecard.BegDate = DateTime.Now;
            timecard.Status = TimecardStatus.Draft;
            person.AddTimecard(timecard);
            GetTimecardDao().Create(timecard);

            // Add allocations
            IList tasks = new ArrayList(GetTaskDao().LoadAll());
            int count = random.Next(1, 4);
            for (int i = 0; i < count; i++)
            {
                TimeAllocation timeAllocation = TimeAllocation.Factory.newInstance();
                timeAllocation.BegTime = timecard.BegDate.Value.Add(new TimeSpan(i, 0, 0));
                timeAllocation.EndTime = timecard.BegDate.Value.Add(new TimeSpan(i + 1, 0, 0));
                Task task = (Task)tasks[random.Next(tasks.Count)];
                task.AddTimeAllocation(timeAllocation);
                timecard.AddTimeAllocation(timeAllocation);
                GetTimeAllocationDao().Create(timeAllocation);
            }

            Console.WriteLine("Timecard " + timecard.Id + " created.");
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
            ICollection people = GetPersonDao().LoadAll();
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
            ICollection tasks = GetTaskDao().LoadAll();
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
            ICollection timecards = GetTimecardDao().LoadAll();
            foreach (Timecard timecard in timecards)
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
        }

        // ----- Helper Methods -----
        enum MenuChoice
        {
            None,
            CreateDatabase,
            AddPerson,
            AddTask,
            AddTimecard,
            ShowData,
            Quit,
            ShowMenu
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
                    case MenuChoice.ShowData:
                        ShowData();
                        break;
                    case MenuChoice.ShowMenu:
                        ShowMenu();
                        break;
                    case MenuChoice.Quit:
                        break;
                }
                SessionManagerFactory.SessionManager.HandleSessionEnd();
            }
        }

        private static void EndApplication()
        {
            SessionManagerFactory.SessionManager.HandleApplicationEnd();
        }

        private static IPersonDao GetPersonDao()
        {
            return DaoFactory.GetPersonDao();
        }

        private static ITaskDao GetTaskDao()
        {
            return DaoFactory.GetTaskDao();
        }

        private static ITimecardDao GetTimecardDao()
        {
            return DaoFactory.GetTimecardDao();
        }

        private static ITimeAllocationDao GetTimeAllocationDao()
        {
            return DaoFactory.GetTimeAllocationDao();
        }

        private static void ShowMenu()
        {
            Console.WriteLine("1. Create Database");
            Console.WriteLine("2. Add Person");
            Console.WriteLine("3. Add Task");
            Console.WriteLine("4. Add Timecard");
            Console.WriteLine("5. Show Data");
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
                    if (intChoice >= 1 && intChoice <= 5)
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