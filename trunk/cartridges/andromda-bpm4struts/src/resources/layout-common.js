
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