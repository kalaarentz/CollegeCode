window.onload = function () {
    cb.getCards(function (cards, error) {
        var node = document.getElementById("container");
        cards.forEach(function (c) {
            var card = document.createElement('div');
            card.setAttribute("class", "card");
            card.setAttribute("id", c.id );

            var deleter = document.createElement('div');
            deleter.setAttribute("class", "deleter");
            deleter.setAttribute("onclick", "remove('" + c.id + "');");

            //anchor
            var anc = document.createElement('a');
            anc.setAttribute("href", c.url);
            var img = document.createElement('img');
            img.setAttribute("src", c.url);

            //append image to anchor
            anc.appendChild(img);

            // text div
            var txt = document.createElement('div');
            txt.setAttribute("class", "text");
            var des = document.createTextNode(c.desc);
            txt.appendChild(des);

            card.appendChild(deleter);
            card.appendChild(anc);
            card.appendChild(txt);
            node.appendChild(card);

        });
    });
};

var addCard = function() {
    var description = document.getElementById("description").value;
    var url = document.getElementById("url").value;

    cb.addCard(url, description, function (cardObj, error) {
        var node = document.getElementById("container");
        // card div
        var card = document.createElement('div');
        card.setAttribute("class", "card");

        //d deleter div
        var deleter = document.createElement('div');
        deleter.setAttribute("class", "deleter");

        //anchor
        var anc = document.createElement('a');
        anc.setAttribute("href", url);
        var img = document.createElement('img');
        img.setAttribute("src", url);

        //append image to anchor
        anc.appendChild(img);

        // text div
        var txt = document.createElement('div');
        txt.setAttribute("class", "text");
        var des = document.createTextNode(description);
        txt.appendChild(des);

        card.appendChild(deleter);
        card.appendChild(anc);
        card.appendChild(txt);
        node.appendChild(card);
    });
};

var remove = function(id){

    cb.removeCard(id, function(card, error) {
        var node = document.getElementById(id);
        var container = document.getElementById("container");
        container.removeChild(node);
    });
};
