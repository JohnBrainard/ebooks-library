<#import "layout/main.ftl" as layouts>
<#import "layout/components.ftl" as components>

<@layouts.main title="Lists" active="lists">

    <div id="search">
        <form method="post" action="/lists">
            <input type="text" name="name"/>
            <input type="submit" value="New List"/>
        </form>
    </div>

    <div id="books">
        <#list lists.lists as list>
            <div class="book">
                <span class="title">
                    <i class="icon-sm">&#x1F4D3;</i>
                    <a href="/lists/${list.id}">${list.name}</a> - ${list.entryCount}
                </span>
            </div>
        </#list>
    </div>
</@layouts.main>
