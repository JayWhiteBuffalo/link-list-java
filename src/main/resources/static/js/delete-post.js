
async function deleteFormHandler(event, id) {
event.preventDefault();
console.log(id)
  const response = await fetch(`/api/posts/${id}`, {
    method: 'DELETE'
  });

  if (response.ok) {
    document.location.replace('/dashboard/')
  } else {
    alert(response.statusText);
  }
}

document.querySelectorAll('.delete-post-btn').forEach(button => {
    button.addEventListener('click', function() {
        let id = this.value;
        deleteFormHandler(event, id)
    });
})
