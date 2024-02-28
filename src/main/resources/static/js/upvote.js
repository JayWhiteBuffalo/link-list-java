
document.querySelector('.upvote-btn').addEventListener('click', upvoteClickHandler);
console.log("Fucktion fired");

async function upvoteClickHandler(event) {
  event.preventDefault();
  console.log("Upvote fired");
  event.currentTarget.classList.toggle('on');
  const id = window.location.toString().split('/')[
    window.location.toString().split('/').length - 1
  ];


  const response = await fetch('/posts/upvote', {
    method: 'PUT',
    body: JSON.stringify({
        postId: id
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



//for (const btn of document.querySelectorAll('.vote')) {
//  btn.addEventListener('click', event => {
//    event.currentTarget.classList.toggle('on');
//  });
//}