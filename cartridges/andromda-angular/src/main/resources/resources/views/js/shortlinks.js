/*
	Shortlinks
	constrains the length of links displayed to a defined length
	written by Chris Heilmann (http://icant.co.uk/)
*/
function shortlinks()
{
	// defines where the link should be cut off, from the right or the middle
	var mode='right';
	// the maximum length of the links
	var mustlength=60; 
	// the string added to or in the middle of the link text after shortening
	var connector='...';
	// the title added to the link, %url% will be replaced with the real link
	var titleadd=' (Full address: %url%)';
	// the links to be shortened
	var contentlinks=document.getElementsByTagName('a');

	// loop over all links
	for(var i=0;i<contentlinks.length;i++)
	{
		// check if the link has an href attribute and content
		if(!contentlinks[i].getAttribute('href') || !contentlinks[i].firstChild){continue;}
		// check if the link starts with http: and that it is longer than the allowed length
		var t=contentlinks[i].firstChild.nodeValue;
		if(/http:/.test(t) && t.length>mustlength)
		{
		// get the text of the link
		// shorten accordingly and add the separator string
			switch(mode){
				case 'middle':
					var newt=t.substr(0,mustlength/2)+connector+t.substr(t.length-mustlength/2-connector.length,t.length);
				break;
				case 'right':
					var newt=t.substr(0,mustlength-connector.length)+connector;
				break;
			}
		// set the title, and replace the original link text
			contentlinks[i].title+=titleadd.replace(/%url%/,t);
			contentlinks[i].replaceChild(document.createTextNode(newt),contentlinks[i].firstChild);
		}
	
	}	
}
// start shortlinks when the window is loaded.
window.onload=shortlinks;
