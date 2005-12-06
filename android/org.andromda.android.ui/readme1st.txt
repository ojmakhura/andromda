Android Readme.

I. How to setup and run Android (if you're a developer)
-------------------------------------------------------
1) Check out the submodules of the "android" module. Make sure they appear in your workspace as individual projects.
2) Create a new launch configuration: 
   a) Run -> Run
   b) Select "Eclipse Application", click "New" and set the Launch config name to something meaningful.
   c) Make sure that on te "plug-ins" Tab, you have selected "Run with all workspace and enabled external plug-ins".
3) Run!

II. Features you should try
---------------------------
1) Create a new AndroMDA project using the New Project Wizard: Choose File -> New -> Project -> AndroMDA Project and 
   follow the on-screen directions.

2) Convert an existing project into an Android project. Use this feature to import your exitsing AndroMDA projects,
   like this: File -> Import -> Convert Java Project to Android Project. The wizard will then list all Java projects
   that are not already an Adroid project. Select one or more and click "Finish". Voila!
   
3) The configuration file editor. Double click an andromda.xml file to open it with the configuration file editor. In 
   order for this feature to work, you must have configured Android correctly. See next point.
   
4) Global and project-specific configuration. Android can be configured using the Window -> Preferences dialog and also
   using the preferences dialog of an Android project. You should first tell Android where to find your cartridges. 
   Please keep in mind to use forward slashes.