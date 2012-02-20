/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function view(id) {
   var elElemento=document.getElementById(id);
   if(elElemento.style.visibility == 'visible') {
      elElemento.style.visibility = 'hidden';
   } 
   else{
      elElemento.style.visibility = 'visible';
   }
 
}

