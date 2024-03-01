
//document.querySelectorAll('.post').forEach(function(button){
//    button.addEventListener('click', function(event){
//        upvoteClickHandler(event);
//    });
//})

document.querySelectorAll('.post').forEach(function(voteCont) {
    voteCont.addEventListener('click', function(event) {
        if (event.target.classList.contains('upvote-btn')) {
            upvoteClickHandler(event);
        } else if (event.target.classList.contains('downvote-btn')) {
            downvoteClickHandler(event);
        }
    });
});

async function upvoteClickHandler(event) {
  event.preventDefault();

  console.log(event);

  let postIteration = event.currentTarget.id;
  console.log(postIteration)

  let postId = postIteration.split('-')[1];

  const response = await fetch('/posts/upvote', {
    method: 'PUT',
    body: JSON.stringify({
        postId: postId
    }),
    headers: {
      'Content-Type': 'application/json'
    }
  });

  if (response.ok) {
    document.location.reload();
  } else {
    alert(response.statusText);
  }
}

async function downvoteClickHandler(event) {
  event.preventDefault();

    console.log("downvote fired")
  console.log(event);

  let postIteration = event.currentTarget.id;
  console.log(postIteration)

  let postId = postIteration.split('-')[1];

  await const response = fetch('/posts/downvote', {
    method: 'PUT',
    body: JSON.stringify({
        postId: postId
    }),
    headers: {
      'Content-Type': 'application/json'
    }
  });

  if (response.ok) {
    window.location.reload();
  } else {
    alert(response.statusText);
  }
}