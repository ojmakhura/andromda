This module contains the artifacts that are NOT deployed remotely, either because the 
license won't allow it (i.e. Sun's License) or it makes it easier to have them in CVS 
(such as the AndroMDA bootstrap artifacts). 

IMPORTANT: Do NOT add any libraries that can be made available remotely!

If you change the artifacts (i.e. meta cartridge) that make up the bootstrap jars, the 
bootstrap artifacts must also be updated and redeployed using mvn -N antrun:run

The maven plugin artifacts (i.e. bootstrap plugin, cartridge plugin) must also be updated.