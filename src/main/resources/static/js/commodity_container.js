class CommodityContainer {
  constructor(container){
    this.ele_container = container;
  }

  addCommodity(commodity_data){
    var commodity = this.createCommodity(commodity_data);
    this.ele_container.append(commodity);
  }

  setElementStyles(styles,ele) {
    var tranStyleName = (style_name) =>{
      return style_name.split('-').map((word,index)=>{
        if(index === 0) return word;
        else return word[0].toLocaleUpperCase() + word.slice(1);
      }).join('');
    }
    var keys = Object.keys(styles);
    for(var key of keys){
      ele.style[tranStyleName(key)] = styles[key];
    }
  }

  initCommodityValue(ele,commodity_data){
    var commodity_name, commodity_desc, commodity_price,
      commodity_pic_url, commodity_cid;
    if(commodity_data){
      commodity_name = commodity_data['name'];
      commodity_desc = commodity_data['desc'];
      commodity_price = commodity_data['price'];
      commodity_pic_url = commodity_data['pic'];
      commodity_cid = commodity_data["cid"];
    }
    ele.setAttribute('id',commodity_cid);
    ele.getElementsByTagName('img')[0].setAttribute("src",commodity_pic_url);
    ele.getElementsByClassName('commodity_name')[0].firstElementChild.innerText = commodity_name;
    ele.getElementsByClassName('commodity_desc')[0].firstElementChild.innerText = commodity_desc;
    var price_children = ele.getElementsByClassName('commodity_price')[0].children;
    price_children[0].innerText = "￥";
    price_children[1].innerText = commodity_price;
    price_children[2].innerText = ".00";
    ele.getElementsByTagName('input')[0].value = 1;
  }

  initCommodityEvent(ele){
    var btn_minus = ele.getElementsByClassName('btn_num_minus')[0];
    var btn_plus = ele.getElementsByClassName('btn_num_plus')[0];
    initNumberMinusPlusBtn(btn_minus,btn_plus);

    var btn_buy = ele.getElementsByClassName('commodity_trade_item')[0];
    var btn_shoppingcart = ele.getElementsByClassName('commodity_trade_item')[1];
    btn_buy.addEventListener('click',()=>{

    })
    btn_shoppingcart.addEventListener('click',()=>{

    })

  }

  createCommodity(commodity_data) {
    var container = this.createCommodityContainer();
    this.initCommodityValue(container,commodity_data);
    this.initCommodityEvent(container);
    return container;
  }


  createCommodityContainer(){
    var createClassEle = (ele_type, classname)=>{
      var ele = document.createElement(ele_type);
      ele.setAttribute('class',classname);
      return ele;
    }
    var total_container = createClassEle('div','commodity');
    var pic_container = createClassEle('div','commodity_pic');
    pic_container.append(document.createElement('img'));

    var desc_container = createClassEle('div','commodity_description');
    var items_container = createClassEle('div','commodity_items');
    var name = createClassEle('div','commodity_item commodity_name');
    name.append(document.createElement('h2'))
    items_container.append(name);

    var desc = createClassEle('div','commodity_item commodity_desc');
    var span = document.createElement('span');
    desc.append(span);
    items_container.append(desc);

    var price = createClassEle('div','commodity_item commodity_price');
    span = document.createElement('span');
    price.append(span);
    span = document.createElement('span');
    price.append(span);
    span = document.createElement('span');
    price.append(span);
    items_container.append(price);

    var number = createClassEle('div','commodity_item commodity_number');
    var div = createClassEle('div','commodity_number');
    var button = createClassEle('button','btn_num_minus');
    button.append(document.createElement('i'));
    div.append(button);
    var input = document.createElement('input');
    input.setAttribute('type','number');
    div.append(input);
    button = createClassEle('button','btn_num_plus');
    button.append(document.createElement('i'));
    div.append(button);
    number.append(div);
    items_container.append(number);

    var func = createClassEle('div','commodity_func');
    div = createClassEle('div','commodity_trade');
    button = createClassEle('button','commodity_trade_item');
    button.innerText = "立即购买";
    div.append(button);
    button = createClassEle('button','commodity_trade_item');
    button.innerText = "放入购物车";
    div.append(button);
    func.append(div);
    items_container.append(func);

    desc_container.append(items_container);
    total_container.append(pic_container);
    total_container.append(desc_container);
    return total_container;
  }
}
