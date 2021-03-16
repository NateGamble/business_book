const OWNER_ID = 3; // using Alex's account as a test

function getOwnerBusinesses() {
    fetch("http://localhost:5000/businesses")
    .then(resp => {
          if (resp.status >= 400) {
              alert("Something went wrong while attempting to grab the businesses!");
              return;
          }

          return resp.json();
    })
    .then(businesses => {
        printBusinesses(businesses/*.filter(business => business.active && business.owner.userId === OWNER_ID)*/);
    });
}

getOwnerBusinesses();

function printBusinesses(businesses) {
    let businessdata = document.querySelector("#businessdata");
    let table = document.createElement("table");
    let tableHeaderRow = document.createElement("tr");

    let headerCell1 = document.createElement("th");
    headerCell1.textContent = "Business Name";

    let headerCell2 = document.createElement("th");
    headerCell2.setAttribute("scope", "col");
    headerCell2.textContent = "Owner Email";

    let headerCell3 = document.createElement("th");
    headerCell3.setAttribute("scope", "col");
    headerCell3.textContent = "Location";

    let headerCell4 = document.createElement("th");
    headerCell4.setAttribute("scope", "col");
    headerCell4.textContent = "Business Type";

    let headerCell5 = document.createElement("th");
    headerCell5.setAttribute("scope", "col");
    headerCell5.textContent = "Update Business";

    let headerCell6 = document.createElement("th");
    headerCell6.setAttribute("scope", "col");
    headerCell6.textContent = "Reset fields";

    tableHeaderRow.appendChild(headerCell1);
    tableHeaderRow.appendChild(headerCell2);
    tableHeaderRow.appendChild(headerCell3);
    tableHeaderRow.appendChild(headerCell4);
    tableHeaderRow.appendChild(headerCell5);
    tableHeaderRow.appendChild(headerCell6);

    table.appendChild(tableHeaderRow);

    businesses.forEach((biz, idx) => {
        let tableBizRow = document.createElement("tr");
        let name = "changeBiz" + idx;

        let businessnameCell = document.createElement("th");
        businessnameCell.setAttribute("scope", "row");
        let bizNameInput = document.createElement("input");
        bizNameInput.setAttribute("type", "text");
        bizNameInput.setAttribute("name", name);
        bizNameInput.setAttribute("value", biz.businessName);
        businessnameCell.appendChild(bizNameInput);

        let emailCell = document.createElement("td");
        let emailInput = document.createElement("input");
        emailInput.setAttribute("type", "email");
        emailInput.setAttribute("name", name);
        emailInput.setAttribute("value", biz.email);
        emailCell.appendChild(emailInput);

        let locationCell = document.createElement("td");
        let locationInput = document.createElement("input");
        locationInput.setAttribute("type", "text");
        locationInput.setAttribute("name", name);
        locationInput.setAttribute("value", biz.location);
        locationCell.appendChild(locationInput);

        let businesstypeCell = document.createElement("td");
        let bizTypeInput = document.createElement("input");
        bizTypeInput.setAttribute("type", "text");
        bizTypeInput.setAttribute("name", name);
        bizTypeInput.setAttribute("value", biz.businessType);
        businesstypeCell.appendChild(bizTypeInput);

        let updateCell = document.createElement("td");
        let updateButton = document.createElement("button");
        updateButton.textContent = "Update";
        updateButton.addEventListener("click", () => updateBusiness(biz, name));
        updateCell.appendChild(updateButton);

        let resetCell = document.createElement("td");
        let resetButton = document.createElement("button");
        resetButton.textContent = "Reset";
        resetButton.addEventListener("click", () => resetBusiness(biz, name));
        resetCell.appendChild(resetButton);

        tableBizRow.appendChild(businessnameCell);
        tableBizRow.appendChild(emailCell);
        tableBizRow.appendChild(locationCell);
        tableBizRow.appendChild(businesstypeCell);
        tableBizRow.appendChild(updateCell);
        tableBizRow.appendChild(resetCell);

        table.appendChild(tableBizRow);
    });

    businessdata.appendChild(table);
}

function updateBusiness(biz, name) {
    let updateRow = document.querySelectorAll('input[name="' + name + '"]');

    biz.businessName = updateRow[0].value;
    biz.email = updateRow[1].value;
    biz.location = updateRow[2].value;
    biz.businessType = updateRow[3].value;

    console.log(biz);
}

function resetBusiness(biz, name) {
    let updateRow = document.querySelectorAll('input[name="' + name + '"]');

    updateRow[0].value = biz.businessName;
    updateRow[1].value = biz.email;
    updateRow[2].value = biz.location;
    updateRow[3].value = biz.businessType;
}