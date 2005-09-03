var bpm4jsfPopupStack = new Array();
var bpm4jsfPopupPosition = -1;
if(navigator.userAgent.indexOf("Gecko") > 0)
{
    browserType = "Gecko";
    document.onclick = executeListeners
}else
{
    browserType = "IE";
    document.onclick = function()
    {
        executeListeners(event)
    }
}
if(typeof(HTMLElement) != "undefined" && !HTMLElement.prototype.insertAdjacentHTML)
{
    HTMLElement.prototype.insertAdjacentHTML = function(where,htmlStr)
    {
        var range=this.ownerDocument.createRange();
        range.setStartBefore(this);
        var parsedHTML=range.createContextualFragment(htmlStr);
        this.appendChild(parsedHTML);
     }
}
function registerClickListener(exec)
{
    bpm4jsfPopupPosition++;
    bpm4jsfPopupStack[bpm4jsfPopupPosition] = exec;
}
function executeListeners(anEvent)
{
    for(ctr = 0; ctr < bpm4jsfPopupStack.length; ctr++)
    {
        eval(bpm4jsfPopupStack[ctr] + "(anEvent);");
    }
}


var popupDisplay;
var popupHorizontalOffset, popupVerticalOffset;
var popupLink,popupFrameForm, popupFrame;
var popupForm;
var popupId;
registerClickListener("hidePopupFrameEvent");
function showPopupFrame(form, link, eventRef, closeId, styleClass, style, hposOffset, 
                        vposOffset, abs, center, height, width, scrolling)
{
	if(popupDisplay == link.id) 
	{ 
	    return;
	}
	else 
	{
	    popupDisplay = link.id;
	}
	popupHorizontalOffset = hposOffset;
	popupVerticalOffset = vposOffset;
	if(document.getElementById)
	{
		if(!popupFrameForm)
		{
			popupFrameWriteSelectorHTML(styleClass, style + ';height: '+height+'; width: '+width+';');
			popupFrameForm=document.getElementById("popupFrameContainer");
			popupFrame=document.getElementById("popupFrameArea");
		}
		popupLink = link;
		popupForm = form;
		popupId = closeId;
		popupFrame.innerHTML = '<iframe src="" name="hiddenPopupFrameTarget" frameborder="0" style="height: ' + height +'; width: ' + width + '; " scrolling="' + scrolling + '" />';
		if (center == true) 
		{
			if(browserType=="Gecko")
			{
				popupFrameForm.style.left = (window.innerWidth - popupFrameForm.clientWidth)/2;
				popupFrameForm.style.top = (window.innerHeight - popupFrameForm.clientHeight)/2;
			} 
			else 
			{
				popupFrameForm.style.left = (screen.width - popupFrameForm.clientWidth)/2;
				popupFrameForm.style.top = (screen.height - popupFrameForm.clientHeight - 90)/2;
			}
		} 
		else 
		{
			if (abs == true) 
			{
				popupFrameForm.style.left=hposOffset;
				popupFrameForm.style.top=vposOffset;
			} 
			else 
			{
				popupFrameForm.style.left = eventRef.clientX+document.body.scrollLeft+document.documentElement.scrollLeft-20+hposOffset;
				popupFrameForm.style.top = eventRef.clientY+document.body.scrollTop+document.documentElement.scrollTop+5+vposOffset;
			}
		}
		popupFrameForm.style.visibility = "visible";
	}
}
function hidePopupFrameEvent(anEvent)
{
	if(popupFrameForm)
	{
		if(browserType == "Gecko")
		{
			var rel=anEvent.target;
		}
		else
		{
			var rel = anEvent.srcElement;
		}
		if(anEvent)
		{
			var icon = popupLink.id;
			while(rel)
			{
				if(rel.id == "popupFrameContainer" || rel.id == icon) 
				{
				    break;
				}
				else 
				{
				    rel = rel.parentNode;
				}
			}
		}
		if(!rel)
		{
			popupForm=null;
			popupId=null;
			hidePopupFrame();
		}
		return;
	}
	else 
	{
	    popupFrameForm = false;
	}
}
function hidePopupFrameRefresh()
{
	eval('popupForm.elements[\'hiddenPopupFrame\'].value=\''+popupId+'\'');
	popupForm.submit();
	hidePopupFrame();
}
function hidePopupFrame()
{
    if(popupFrameForm)
    {
        popupFrameForm.style.visibility = "hidden";
        popupDisplay = null;
    }
    else
    {
        popupFrameForm=false
    }
}
function popupFrameWriteSelectorHTML(styleClass, style)
{
    var selectorHTML = '<table border="0" cellpadding="0" cellspacing="0" class="' + styleClass + '" style="' + style + '" id="popupFrameContainer"><tr><td id="popupFrameArea"></td></tr></table>';document.body.insertAdjacentHTML("BeforeEnd",selectorHTML)
}