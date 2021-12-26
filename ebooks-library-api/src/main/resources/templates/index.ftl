<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eBooks Library</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>

<div id="header">
    <h1><i class="icon large">&#128218;</i>eBook Library</h1>
</div>

<div id="root">

    <ul>
        <#list collections as collection>
            <ul>
                <a href="/collection/${collection.id}">${collection.name}</a>
            </ul>
        </#list>
    </ul>

</div>

</body>
</html>
