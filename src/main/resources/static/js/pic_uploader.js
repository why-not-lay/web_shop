class PicUploader{
  constructor(container){
    this.container = container;
    this.binding_item = null;
    this.parent_container = null;
    this.initGlobalEvent();
  }

  clearPath(){
    this.container.getElementsByTagName('input')[0].value = "";
  }

  initGlobalEvent(){
    var input = this.container.getElementsByTagName('input');
    input[2].addEventListener('click',()=>{

      var cid = this.binding_item.getAttribute('cid');
      input[1].value = cid;
      this.parent_container.setPic("/pic/get?cid="+cid);
      this.closeContainer();
    })
    this.container.getElementsByTagName('button')[0].addEventListener('click',()=>{
      this.clearPath();
      this.closeContainer();
    })
  }

  bindItem(container, item){
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

  closeContainer(){
    this.setCloseContainer(true);
  }

  displayContainer(){
    this.clearPath();
    this.setCloseContainer(false);
  }

}
