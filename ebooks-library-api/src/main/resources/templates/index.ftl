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
    <div id="books">
        <#list collections as collection>
            <div class="book">
                <span class="title">
                    <i class="icon-sm">&#128218;</i>
                    <a href="/collection/${collection.id}">${collection.name}</a>
                </span>
            </div>
        </#list>
    </div>
</div>

</body>
</html>
