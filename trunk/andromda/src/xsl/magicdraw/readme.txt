MagicDraw's XMI appears to have one bug.

The multiplicy range for associations should only contains numbers.  
The MagicDraw output has '*' in the slot for the upper range for 1 to many assocation.
The XSL file in this directory will find the '*' and replace them with a '-1'.

MagicDraw support has said they will fix this bug in their next release.

Anthony Mowers.