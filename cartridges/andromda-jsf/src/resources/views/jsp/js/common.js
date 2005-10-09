function getElementValueById(id)
{
    var element = getDiv(id);
    if(element.value)
        return element.value

    return false;
}

function getElementById(elementId)
{
    //Netscape layers
    if(document.layers) 
    {
        return document.layers[elementId];
    }

    //DOM; IE5, NS6, Mozilla, Opera
    if(document.getElementById)
    { 
        return document.getElementById(elementId);
    }

    //Proprietary DOM; IE4
    if(document.all) 
    {
        return document.all[elementId];
    }

    //Netscape alternative
    if(document[elementId]) 
    {
        return document[elementId];
    }

    return false;
}

function openWindow(href,name,centered,attributes,width,height)
{
    var w = (width) ? width : 400;
    var h = (height) ? height : 300;

    var windowWidth = (document.all) ? document.body.clientWidth : window.outerWidth;
    var windowHeight = (document.all) ? document.body.clientHeight : window.outerHeight;

    x = (windowWidth-w) / 2;
    y = (windowHeight-h) / 2;

    var properties = (attributes)
        ? "toolbar=yes,directories=yes,status=yes,menubar=yes,copyhistory=yes,location=yes"
        : "toolbar=no,directories=no,status=no,menubar=no,copyhistory=no,location=no";

    properties = "width="+w+",height="+h+",top="+y+",left="+x+",scrollbars=yes,resizable=yes," + properties;

    window.open(href,name,properties);
}

function focusOnElementById(elementId)
{
    var field = getElementById(elementId);
    if (field)
    {
        field.focus();
        field.select();
    }
}