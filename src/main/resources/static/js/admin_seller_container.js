class SellerContainer{
  constructor(container){
    this.container = container;
    this.initEvent();
  }

  setCloseContainer(is_close){
    if(is_close){
      this.container.style.display = "none";
    }
    else{
      this.container.style.display = "block";
    }
  }

  isForPassword(is_for_password){
    if(is_for_password){
      this.container.getElementsByTagName('input')[0].setAttribute('readonly','readonly');
    }
    else{
      this.container.getElementsByTagName('input')[0].removeAttribute('readonly');
    }
  }

  initEvent(){
    var buttons = this.container.getElementsByTagName('button');

    buttons[0].addEventListener('click',()=>{
      this.closeContainer();
    })
    buttons[1].addEventListener('click',()=>{
      // 发送请求:  <28-05-21, yourname> //
    })
  }

  displayContainer(){
    this.setCloseContainer(false);
  }

  closeContainer(){
    this.setCloseContainer(true);
  }

  setValue(data){
    if(!data) return;
    var title = data['title'];
    var seller_name = data['name'];
    this.container.getElementsByTagName('h2')[0].innerText = title;
    this.container.getElementsByTagName('input')[0].value = seller_name;
  }
}
