function getAllUsers() {
   fetch("http://localhost:5000/users")
  .then(resp => {
        if (resp.status >= 400) {
            alert("Something went wrong while attempting to grab the users!");
            return;
        }

        return resp.json();
  })
  .then(users => {
       printAppUsers(users.filter(user => user.active));
  });
}

getAllUsers();

function getAllBusinesses() {
    fetch("http://localhost:5000/businesses")
    .then(resp => {
          if (resp.status >= 400) {
              alert("Something went wrong while attempting to grab the businesses!");
              return;
          }

          return resp.json();
    })
    .then(businesses => {
        printBusinesses(businesses);
    });
}

getAllBusinesses();

function usersClosure(usersParam) {
    let users = usersParam;

    return function() {
        return users;
    }
}

function printAppUsers(users) {
    console.log(users);
    let userdata = document.querySelector("#userdata");
    let table = document.createElement("table");
    let tableHeaderRow = document.createElement("tr");

    let headerCell1 = document.createElement("th");
    headerCell1.textContent = "Username";

    let headerCell2 = document.createElement("th");
    headerCell2.setAttribute("scope", "col");
    headerCell2.textContent = "Email";

    let headerCell3 = document.createElement("th");
    headerCell3.setAttribute("scope", "col");
    headerCell3.textContent = "Firstname";

    let headerCell4 = document.createElement("th");
    headerCell4.setAttribute("scope", "col");
    headerCell4.textContent = "Lastname";

    let headerCell5 = document.createElement("th");
    headerCell5.setAttribute("scope", "col");
    headerCell5.textContent = "Delete User";

    tableHeaderRow.appendChild(headerCell1);
    tableHeaderRow.appendChild(headerCell2);
    tableHeaderRow.appendChild(headerCell3);
    tableHeaderRow.appendChild(headerCell4);
    tableHeaderRow.appendChild(headerCell5);

    table.appendChild(tableHeaderRow);

    users.forEach(users => {
        let tableUserRow = document.createElement("tr");

        let usernameCell = document.createElement("th");
        usernameCell.setAttribute("scope", "row");
        usernameCell.textContent = users.username;

        let emailCell = document.createElement("td");
        emailCell.textContent = users.email;

        let firstnameCell = document.createElement("td");
        firstnameCell.textContent = users.firstName;

        let lastnameCell = document.createElement("td");
        lastnameCell.textContent = users.lastName;

        let deleteCell = document.createElement("td");
        let deleteBtn = document.createElement("button");
        deleteBtn.addEventListener("click", () => deleteUser(users.userId));
        deleteBtn.textContent = "Delete";
        deleteCell.appendChild(deleteBtn);

        tableUserRow.appendChild(usernameCell);
        tableUserRow.appendChild(emailCell);
        tableUserRow.appendChild(firstnameCell);
        tableUserRow.appendChild(lastnameCell);
        tableUserRow.appendChild(deleteCell);

        table.appendChild(tableUserRow);
    });

    userdata.appendChild(table);
}

function printBusinesses(businesses) {
    console.log(businesses);
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
    headerCell5.textContent = "Delete Business";

    let headerCell6 = document.createElement("th");
    headerCell6.setAttribute("scope", "col");
    headerCell6.textContent = "Show Reviews";

    tableHeaderRow.appendChild(headerCell1);
    tableHeaderRow.appendChild(headerCell2);
    tableHeaderRow.appendChild(headerCell3);
    tableHeaderRow.appendChild(headerCell4);
    tableHeaderRow.appendChild(headerCell5);
    tableHeaderRow.appendChild(headerCell6);

    table.appendChild(tableHeaderRow);

    businesses.forEach(biz => {
        let tableBizRow = document.createElement("tr");

        let businessnameCell = document.createElement("th");
        businessnameCell.setAttribute("scope", "row");
        businessnameCell.textContent = biz.businessName;

        let emailCell = document.createElement("td");
        emailCell.textContent = biz.email;

        let locationCell = document.createElement("td");
        locationCell.textContent = biz.location;

        let businesstypeCell = document.createElement("td");
        businesstypeCell.textContent = biz.businessType;

        let deleteCell = document.createElement("td");
        let deleteBtn = document.createElement("button");
        deleteBtn.addEventListener("click", () => deleteBusiness(biz.id));
        deleteBtn.textContent = "Delete";
        deleteCell.appendChild(deleteBtn);

        let reviewCell = document.createElement("td");
        let reviewBtn = document.createElement("button");
        reviewBtn.addEventListener("click", () => showReviewsByBusiness(biz));
        reviewBtn.textContent = "Show";
        reviewCell.appendChild(reviewBtn);

        tableBizRow.appendChild(businessnameCell);
        tableBizRow.appendChild(emailCell);
        tableBizRow.appendChild(locationCell);
        tableBizRow.appendChild(businesstypeCell);
        tableBizRow.appendChild(deleteCell);
        tableBizRow.appendChild(reviewCell);

        table.appendChild(tableBizRow);
    });

    businessdata.appendChild(table);
}

function showReviewsByBusiness(biz) {
    let reviews = document.querySelector("#reviewsdata");

    while (reviews.firstChild) {
        reviews.removeChild(reviews.firstChild);
    }

    if (!biz.reviews || biz.reviews.length === 0) {
        let errorHeader = document.createElement("h1");
        errorHeader.setAttribute("id", "errorHeader");
        errorHeader.textContent = "No reviews found for " + biz.businessName;
        reviews.appendChild(errorHeader);

        return;
    }

    let table = document.createElement("table");
    let tableHeaderRow = document.createElement("tr");

    let headerCell1 = document.createElement("th");
    headerCell1.textContent = "Rating";

    let headerCell2 = document.createElement("th");
    headerCell2.setAttribute("scope", "col");
    headerCell2.textContent = "Review";

    let headerCell3 = document.createElement("th");
    headerCell3.setAttribute("scope", "col");
    headerCell3.textContent = "Delete Review";

    tableHeaderRow.appendChild(headerCell1);
    tableHeaderRow.appendChild(headerCell2);
    tableHeaderRow.appendChild(headerCell3);

    table.appendChild(tableHeaderRow);

    biz.reviews.forEach(review => {
        let tableReviewRow = document.createElement("tr");

        let ratingCell = document.createElement("th");
        ratingCell.setAttribute("scope", "row");
        ratingCell.textContent = review.rating;

        let reviewCell = document.createElement("td");
        reviewCell.textContent = review.review;

        let deleteCell = document.createElement("td");
        let deleteBtn = document.createElement("button");
        deleteBtn.addEventListener("click", () => deleteReview(review.id));
        deleteBtn.textContent = "Delete";
        deleteCell.appendChild(deleteBtn);

        tableReviewRow.appendChild(ratingCell);
        tableReviewRow.appendChild(reviewCell);
        tableReviewRow.appendChild(deleteCell);

        table.appendChild(tableReviewRow);
    });

    reviews.appendChild(table);
}

function deleteBusiness(bizId) {
    let deleteUrl = "http://localhost:5000/businesses/id/" + bizId;
    console.log(deleteUrl);
    /*fetch(deleteUrl, {
        method: 'DELETE'
    })
    .then(res => {
        if (res.status < 400) {
            getAllBusinesses();
        }
    });*/
}

function deleteUser(userId) {
    let deleteUrl = "http://localhost:5000/users/id/" + userId;
    console.log(deleteUrl);
    getAllUsers();
    /*fetch(deleteUrl, {
        method: 'DELETE'
    })
    .then(res => {
        if (res.status < 400) {
            getAllUsers();
        }
    });*/
}

function deleteReview(reviewId) {
    let deleteUrl = "http://localhost:5000/businesses/reviews/id/" + reviewId;
    console.log(deleteUrl);

    /*fetch(deleteUrl, {
        method: 'DELETE'
    })
    .then(res => {
        if (res.status < 400) {
            getAllBusinesses();
        }
    });*/
}

