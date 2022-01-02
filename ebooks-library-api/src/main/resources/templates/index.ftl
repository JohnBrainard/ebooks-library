<#import "layout/main.ftl" as main>

<@main.main active="collections">
    <div id="search">
        <form method="get" action="/search">
            <input type="text" name="title"/>
            <input type="submit" value="Search"/>
        </form>
    </div>

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

    <div style="padding: 1em;">
        <form method="post" action="/index">
            <input type="submit" value="Reindex Library"/>
        </form>
    </div>
</@main.main>
