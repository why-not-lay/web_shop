class SellerContainer{
  constructor(container){
    this.container = container;
    this.initEvent();
    this.parent_container = null;
    this.binding_item = null;
  }

  bindItem(container,item){
    this.parent_container = container;
    this.binding_item = item;
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

  certifyPassword(){
    var pw = this.container.getElementsByTagName('input')[1].value;
    var pw_c = this.container.getElementsByTagName('input')[2].value;
    return pw === pw_c;
  }

  initEvent(){
    var buttons = this.container.getElementsByTagName('button');

    buttons[0].addEventListener('click',()=>{
      this.closeContainer();
    })
    buttons[1].addEventListener('click',()=>{
      if(!this.certifyPassword()){
        window.alert("密码不一致，请重新输入");
        this.container.getElementsByTagName('input')[1].value = "";
        this.container.getElementsByTagName('input')[2].value = "";
        return;
      }
      var data = this.getValue();
      if(data['is_restet']){
        var url = getUrlByType('shop','reset');
        PostJSON(url,{
          "uid":this.binding_item.getAttribute('uid'),
          "password":this.container.getElementsByTagName('input')[1].value
        }).then((json)=>{
          if(json['code'] === 200){
            console.log("重置密码成功");
          }
          else{
            console.log("重置密码失败");
          }
        });
      }
      else{
        var url = getUrlByType('shop','register');
        PostJSON(url,{
          "username":this.container.getElementsByTagName('input')[0].value,
          "password":this.container.getElementsByTagName('input')[1].value
        }).then((json)=>{
          if(json['code'] === 200){
            console.log("注册成功");
            this.parent_container.flushItems();
          }
          else{
            console.log("注册失败");
          }
        });
      }
      this.closeContainer();
    })
  }

  displayContainer(){
    this.setCloseContainer(false);
  }

  closeContainer(){
    this.setCloseContainer(true);
  }

  getValue(){
    var is_restet =this.container.getElementsByTagName('h2')[0].innerText === "创建销售员" ? false : true;
    var input = this.container.getElementsByTagName('input');
    return {
      "name":input[0].value,
      "password":input[1].value,
      "confirm":input[2].value,
      "is_restet":is_restet
    }
  }

  setValue(data){
    if(!data) return;
    var title = data['title'];
    var seller_name = data['name'];
    this.container.getElementsByTagName('h2')[0].innerText = title;
    this.container.getElementsByTagName('input')[0].value = seller_name;
    this.container.getElementsByTagName('input')[1].value = "";
    this.container.getElementsByTagName('input')[2].value = "";
  }
}
