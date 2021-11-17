/*let cambioColor = document.querySelector(".change-color");   
cambioColor.addEventListener("click",myf1);
let contador = 0;
 function myf1() {
	
      if (contador % 2 ==0) {
          cambioColor.style.backgroundColor= "black";
      } else {
         cambioColor.style.backgroundColor = "white";
      }
      contador += 1;
    };*/
	const btnSwitch = document.querySelector('#switch');
    const vista = document.querySelector('.vista-principal');
//    const carro = document.querySelector('.carousel', 'h2');
    
    btnSwitch.addEventListener('click', () => {
		document.body.classList.toggle('dark');
		vista.classList.toggle('dark');
//		carro.classList.toggle('dark');
		btnSwitch.classList.toggle('active');
		
	//GUARDAMOS EN LOCAL STORAGE
	if(vista.classList.contains('dark') || document.body.classList.contains('dark')){
		localStorage.setItem('dark-mode','true');		
	}
	
	else {
		localStorage.setItem('dark-mode','false');
	}
	});
	
	if(localStorage.getItem('dark-mode')== 'true' ||document.body.classList.toggle('dark-mode')=='true'){
		vista.classList.add('dark');
//		carro.classList.add('dark');
		btnSwitch.classList.add('active');
		document.body.classList.add('dark');
	}else {
		vista.classList.remove('dark');
//		carro.classList.remove('dark');
		btnSwitch.classList.remove('active');
		document.body.classList.remove('dark');
	}