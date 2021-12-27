<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eBooks Library</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

<div id="header">
    <h1>
        <a href="/" style="text-decoration: none"><i class="icon large">&#128218;</i></a>
        eBook Library - ${collection.name}
    </h1>
</div>

<div id="root">
    <h2>${collection.name}</h2>

    <div id="books">
        <#list collection.entries as entry>
            <div class="book">
                <span class="title">
                    <a href="${entry.downloadUrl}">
                        ${entry.title}
                    </a>
                </span>
                <span class="fileName">${entry.name}</span>
            </div>
        </#list>
    </div>
</div>

</body>
</html>
