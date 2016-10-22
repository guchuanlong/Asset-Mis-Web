 //替换字符串  
    function Replace(str, from, to) {
        return str.split(from).join(to);
    }
    // 日期类型格式成指定的字符串
    function FormatDate(date, format) {
        format = Replace(format, "yyyy", date.getFullYear());
        format = Replace(format, "MM", GetFullMonth(date));
        format = Replace(format, "dd", GetFullDate(date));
        format = Replace(format, "HH", GetFullHour(date));
        return format;
    }
    //js日期字符串转换成日期类型
    function parseDate(dateStr) {
        return new Date(Replace(dateStr, "-", "/"));
    }
    function addYear(date,value)
    {
    	date.setFullYear(date.getFullYear()+value);
    	return date;
    }
    
    //增加月  
    function AddMonths(date, value) {
        date.setMonth(date.getMonth() + value);
        return date;
    }
    //增加天  
    function AddDays(date, value) {
        date.setDate(date.getDate() + value);
        return date;
    }
    //增加时
    function AddHours(date, value) {
        date.setHours(date.getHours() + value);
        return date;
    }
    //返回月份(两位数)  
    function GetFullMonth(date) {
        var v = date.getMonth() + 1;
        if (v > 9) return v.toString();
        return "0" + v;
    }
 
    //返回日(两位数)  
    function GetFullDate(date) {
        var v = date.getDate();
        if (v > 9) return v.toString();
        return "0" + v;
    }
    //返回时(两位数)
    function GetFullHour(date) {
        var v = date.getHours();
        if (v > 9) return v.toString();
        return "0" + v;
    }
    //比较两个时间
    function compareDate(date) {
        var mydate = parseDate(date);
        var nowdate = new Date();
        if (nowdate.getTime() < mydate.getTime()) {
            return true
        }
        return false;
    }