<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Wage Summary</title>
</head>
<body>
    Wage Summary for period <c:out value='${monthYear}' />
   <table border="1">
    <tr>
        <td>Employee ID</td>
        <td>Employee Name</td>
        <td>Employee Wage</td>
     </tr>
    <c:forEach var="employeeResult" items="${monthResult}">
     <tr>
         <td><c:out value="${employeeResult.key}"/></td>
         <td><c:out value="${employeeResult.value.employeeName}"/></td>
         <td><c:out value="${employeeResult.value.totalWage}"/>$</td>
     </tr>
</c:forEach>
</table>
</body>
</html>