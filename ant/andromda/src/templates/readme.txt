NOTE: This AndroMDA Ant Tool is an option for those users who don't want to use Maven.
The maven-andromda-plugin in this distribution has a goal called "andromda:genapp"
which will setup an AndroMDA Maven application.

To generate an AndroMDA Ant J2EE application you must takes the following steps:

Run the Ant build located in this directory: type 'ant'.  This will run 
the andromda-ant tool as well as create a directory called 'lib' containing all 
necessary dependencies for your newly generated application. 

To successfully build your new application, you must do two things:

1) Copy the resulting lib directory to the directory of your newly generated application. 
2) Change to your newly generated application directory and type 'ant' and you'll
   see your new AndroMDA application Ant application being built!
   
Good Luck!

-- The AndroMDA Team