Prerequisite:
* rename the "build.properties.sample" file in this directory to "build.properties"
* then edit it to reflect your particular environment.
* the following must present on the same level as this directory:
-andromda
-samples
-cartridges

In order for this regression test to be useful, it must be
run AT LEAST twice. The first run of the default target will
create the initial "drafts" directory of this module (these are 
the files that will be compared against on any following runs). 
During any run after, the generated directory will be
created and compared against the "drafts" from the first run.  

Steps to see impact of modification to AndroMDA core source code or cartridge source code:
1) Run regression build file using default target
2) Make change to either cartridge or AndroMDA core source
3) Perform step 1 again
4) Check generated JUnit report in the build/reports directory of this module for any differences 
   in generated output. (If you see any failures, you know your modification(s) changed the generated source)

Enjoy!

Chad Brandon
