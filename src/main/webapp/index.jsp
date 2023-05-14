<% 
if (session.getAttribute("name")==null){
response.sendRedirect("login.jsp");
}
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="utf-8">
    <title>Accueil - Robbie Lens Photographie</title>
    <link rel="stylesheet" href="page.css"> 
    <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@200&family=Montserrat:ital,wght@0,200;0,300;1,200&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Josefin+Sans:wght@200&family=Manrope:wght@200&family=Montserrat:ital,wght@0,200;0,300;1,200&display=swap" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Manrope&family=Montserrat&display=swap" rel="stylesheet">
</head>

<body>
    <header>
        <nav>
            <div class="entete">
            <img src="image/logo.png" alt="Logo Robbie Lens"></div>
            <div>
			<c:if test='${users == "super"}'>
    			<a href="consulter">Consulter</a>
			</c:if>
			<c:if test='${users == "admin"}'>
    			<a href="consulter">Consulter</a>
			</c:if>
            <a href="logout">Se d�connecter</a>
            </div>
        </nav>
    </header>
<main>
    <section class="accueil-introduction">
        <div>
            <h1>Robbie Lens Laboratoire</h1>
            <p>
                Au laboratoire de recherche fictif Robbie Lens, une �quipe de scientifiques travaille sur le d�veloppement de technologies avanc�es pour am�liorer la vie quotidienne des gens. Ils explorent des id�es innovantes et cherchent � cr�er des solutions pratiques pour les d�fis du futur. Leur travail est guid� par la curiosit�, la cr�ativit� et l'imagination.
            </p>
            
                <a href="" class="cta">UN PROJET ? �crivez-moi</a>
        </div>
        <img src="image/Init.jpg" alt="Portrait avec effet de la photographe Robbie Lens" >
    </section>
    <section class="accueil-photos">
    	<h2>Binevenu Mensieur ${users}</h2>
        <h2>Laboratoire de recherche </h2>
        <div>
            <img src="image/labo.jpg" alt="Twelve apostles - Australie" >
            <img src="image/labo2.jpg" alt="Wai-O-Tapu - Nouvelle-Zélande" >
            <img src="image/labo3.jpg" alt="Parc National d’Abel Tasman - Nouvelle-Zélande" >
        </div>
        <div>
            <img src="image/labo7.jpg" alt="Lac Rotorua - Nouvelle-Zélande" >
            <img src="image/labo5.jpg" alt="Milford Sound - Nouvelle-Zélande" >
            <img src="image/labo6.jpg" alt="Lac Wanaka - Nouvelle-Zélande" >
        </div>
    </section>
</main>
<footer>
    <img src="image/logo.png" alt="Logo Robbie Lens" >
    <div>
        <a target="_blank" href="https://twitter.com/" class="lien-icone">
            <img src="image/twitter.png" alt="Logo Twitter" >
        </a>
        <a target="_blank" href="https://www.instagram.com/" class="lien-icone">
            <img src="image/instagram.png" alt="Logo Instagram" >
        </a>
    </div>
    </div>
</footer>
</body>

</html>