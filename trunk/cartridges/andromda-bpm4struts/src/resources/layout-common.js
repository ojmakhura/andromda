function getElementValueById(id)
{
    var element = getDiv(id);
    if(element.value)
        return element.value

    return false;
}

function getDiv(divID)
{
    if( document.layers ) //Netscape layers
        return document.layers[divID];

    if( document.getElementById ) //DOM; IE5, NS6, Mozilla, Opera
        return document.getElementById(divID);

    if( document.all ) //Proprietary DOM; IE4
        return document.all[divID];

    if( document[divID] ) //Netscape alternative
        return document[divID];

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
