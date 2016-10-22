function isemail(str)
{
var result=str.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);
if(result==null) return false;
return true;
}


function isMobile(str)
{
	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/; 
	 if (reg.test(str)) { 
	      return true;
	 }else{ 
	    return false;
	 };	
}


function isTel(str)
{
//国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
 var pattern =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
 //var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/; 
    if(str!="")
     {
         if(!pattern.exec(str))
         {
          return false;
         }
         else
        	 {
        	 return true;
        	 }
     }
    return false;
}



function testisNum(str)
{
	 var g = /^[1-9]*[1-9][0-9]*$/;
	    return g.test(str);
 }



function isCardNo(card)  
{  
   // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
   if(reg.test(card) === false)  
   {   
      return  false;  
   }  
   return true;
}
