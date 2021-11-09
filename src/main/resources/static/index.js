 let cambioColor = document.querySelector(".change-color");   
cambioColor.addEventListener("click",myf1);
let contador = 0;
 function myf1() {
	
      if (contador % 2 ==0) {
          cambioColor.style.backgroundColor= "black";
      } else {
         cambioColor.style.backgroundColor = "white";
      }
      contador += 1;
    };
