//收据打印
function printReceipt(basepath,orderId) {
	var url = basepath+"/receipt/print?orderId=" + orderId;
	var height=window.screen.availHeight-50;
	var width=window.screen.availWidth-10;
	window.open(url,'收据打印','top=0,left=0,height='+height+',width='+width+', toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
}