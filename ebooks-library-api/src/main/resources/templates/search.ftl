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
        eBook Library - Search Books
    </h1>
</div>

<div id="root">
    <h2>Search Results: ${query}</h2>

    <div id="search">
        <form method="get" action="/search">
            <input type="text" name="title"/>
            <input type="submit" value="Search"/>
        </form>
    </div>

    <div id="books">
        <#list results.results as entry>
            <div class="book">
                <span class="title">
                    <a href="${entry.downloadUrl}" target="_blank">
                        <i class="icon-sm">&#x1F4D6;</i> ${entry.title}
                    </a>
                </span>
                <span class="fileName">
                    <a href="${entry.collectionUrl}">
                        ${entry.collectionName}
                    </a>
                </span>
            </div>
        </#list>
    </div>
</div>

</body>
</html>
