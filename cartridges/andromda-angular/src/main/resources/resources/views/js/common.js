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

function focusOnElementById(elementId)
{
    var field = getElementById(elementId);
    if (field)
    {
        field.focus();
        if (field.select)
        {
            field.select();
        }
    }
}