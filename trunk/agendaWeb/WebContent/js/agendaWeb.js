function confirmaRemover(url) {
  if (confirm("Deseja realmente remover o registro?")) {
    document.location = url;
  }
}

function controla(nomeDiv) 
{ 
    if( document.getElementById(nomeDiv).style.visibility == "hidden" ) 
    { 
        document.getElementById(nomeDiv).style.visibility = "visible"; 
    } 
    else 
    { 
        document.getElementById(nomeDiv).style.visibility = "hidden"; 
    } 
}