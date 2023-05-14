<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Manrope&family=Montserrat&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
        <link rel="stylesheet" href="page.css"> 
    <meta charset="utf-8">
    <title>À propos - Robbie Lens Laboratoire</title>
</head>

<body>
    <header>
        <nav>
            <a href="index.jsp" class="lien-icone">
                <img src="image/logo.png" alt="Logo Robbie Lens">
            </a>
           
            <div>
              <!--  <a href="index.jsp">Accueil</a>-->
                 <a href="consulter">Consulter</a>
                <a href="logout">Se déconnecter</a>
            </div>
        </nav>   
    </header>
<main class="a-propos-main">
    <section>
    <div   >
    <table id="example" class="table table-bordered table-white" style="color: black; background-color: white; padding: 20px;" >
  <thead>
    <tr>
      <th>Nom</th>
      <th>Prenom</th>
      <th>Username</th>
      <th>Email</th>
      <th>Mobile</th>
      <th><th></th></th>
    </tr>
  </thead>
  <tbody>
  <c:forEach items="${users}" var="user">
    <tr>    
      <td>${user.nom}</td>
      <td>${user.prenom}</td> 
      <td>${user.name}</td>
      <td>${user.email}</td>
      <td>${user.mobile}</td>
      <td><button type="button" class="btn btn-success">Update</button></td>
      <td> <a href="Delete?scroll=${user.name}"><input type="button" class="btn btn-danger" value="Delete"></a></td>
    </tr>
   </c:forEach>
  </tbody>
</table>
    </div>
</section>
</main>
<footer>
    <a href="index.html" class="lien-icone" >
        <img src="image/logo.png" alt="Logo Robbie Lens">
    </a>
   <div> 
        <a target="_blank"  href="https://twitter.com/" class="lien-icone">
            
            <img src="image/twitter.png" alt="Logo Twitter">
        </a>
        <a target="_blank"  href="https://www.instagram.com/" class="lien-icone">
            
            <img src="image/instagram.png" alt="Logo Instagram">
        </a>
    </div>
</footer>
</body>

<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>
<script>$(document).ready(function () {
    $('#example').DataTable();
});</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
</html>