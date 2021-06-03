class CommodityDetail {
  constructor(container,pic_uploader_container){
    this.container = container;
    this.pic_uploader_container = pic_uploader_container;
    this.initEvent();
    this.binding_item = null;
    this.parent_container = null;
  }

  bindItem(container, item){
    this.parent_container = container;
    this.binding_item = item;
  }

  setValue(data){
    var title = data['title'];
    var name = data['name'];
    var price = data['price'];
    var number = data['number'];
    var type = data['type']? data['type'] : "0";
    var desc = data['desc'];
    var cid = this.binding_item.getAttribute('cid');

    document.getElementById('create_title').getElementsByTagName('h2')[0].innerText = title;
    document.getElementById('create_desc_name').getElementsByTagName('input')[0].value = name;
    document.getElementById('create_desc_price').getElementsByTagName('input')[0].value = price;
    document.getElementById('create_desc_number').getElementsByTagName('input')[0].value = number;
    document.getElementById('create_desc_type').getElementsByTagName('select')[0].value = type;
    document.getElementById('create_desc_description').getElementsByTagName('textarea')[0].value = desc;
    this.setPic("/pic/get?cid="+cid);
  }

  getValue(){
    var is_new = document.getElementById('create_title').getElementsByTagName('h2')[0].innerText === "新建商品" ? true : false;
    var name = document.getElementById('create_desc_name').getElementsByTagName('input')[0].value;
    var price = document.getElementById('create_desc_price').getElementsByTagName('input')[0].value;
    var number = document.getElementById('create_desc_number').getElementsByTagName('input')[0].value;
    var type = document.getElementById('create_desc_type').getElementsByTagName('select')[0].value;
    var desc = document.getElementById('create_desc_description').getElementsByTagName('textarea')[0].value;
    return {
      "name":name,
      "price":price,
      "number":number,
      "type":type,
      "desc":desc,
      "is_new":is_new
    }
  }

  setPic(pic_url){
    this.container.getElementsByTagName('img')[0].setAttribute('src',pic_url);
  }

  initEvent(){
    //var buttons = this.container.getElementsByTagName('button');
    var btn_upload = this.container.getElementsByClassName('create_btn_for_upload');
    var btn_cancel = this.container.getElementsByClassName('create_btn_for_cancel');
    var btn_save = this.container.getElementsByClassName('create_btn_for_save');
    var btn_close = document.getElementById('create_container_close');

    btn_close && btn_close.addEventListener('click',()=>{
      this.closeContainer();
    })
    btn_upload[0].addEventListener('click',()=>{
      this.pic_uploader_container.bindItem(this,this.binding_item);
      this.pic_uploader_container.displayContainer();
    })
    btn_cancel[0].addEventListener('click',()=>{
      this.closeContainer();
    })
    btn_save[0].addEventListener('click',()=>{
      var data = this.getValue();
      if(data['is_new']){
        var add_url = getUrlByType("commodity","add");
        PostJSON(add_url,{
          "shopname":data['name'],
          "price":data['price'],
          "number":data['number'],
          "type":data['type'],
          "description":data['desc'],
        }).then((json)=>{
          if(json['code'] === 200){
            console.log(add_url+"商品添加成功");
            this.parent_container.flushItems();
          }
          else{
            console.log(add_url+"商品添加失败");
          }
        })
      }
      else{
        this.parent_container.setItemValue(this.binding_item,{
          "name":data['name'],
          'price':data['price'],
          'number':data['number'],
          'type':data['type'],
          'desc':data['desc']
        })
      }
      this.closeContainer();
    })
  }

  setCloseContainer(is_close){
    if(is_close){
      this.container.style.display = "none";
    }
    else{
      this.container.style.display = "block";
    }
  }

  closeContainer(){
    this.setCloseContainer(true);
    if(this.parent_container instanceof CommodityContainer){
      docCookies.setItem('ve',new Date().getTime());
      var url = getUrlByType('view',"");
      getJSON(url).catch((error)=>{
        console.log(error);
      })
    }
  }

  displayContainer(){
    this.setCloseContainer(false);
    if(this.parent_container instanceof CommodityContainer){
      docCookies.setItem('vc',this.binding_item.getAttribute('cid'));
      docCookies.setItem('vb',new Date().getTime());
    }
  }

  setEditable(is_editable, is_number_editable){
    var ele_func = document.getElementById('create_func_container');
    var eles_input = this.container.getElementsByTagName('input');
    var ele_textarea = this.container.getElementsByTagName('textarea')[0]
    var ele_select = this.container.getElementsByTagName('select')[0]
    if(is_editable){
      ele_func.style.display = "flex";
      ele_textarea.removeAttribute('readonly');
      ele_select.removeAttribute('disabled');
    }
    else{
      ele_func.style.display = "none";
      ele_textarea.setAttribute('readonly','readonly');
      ele_select.setAttribute('disabled','disabled');
    }

    for(let input of eles_input){
      if(is_editable){
        input.removeAttribute('readonly');
      }
      else{
        input.setAttribute('readonly','readonly');
      }
    }
    if(is_number_editable){
      eles_input[2].removeAttribute('readonly');
    }
  }

}
