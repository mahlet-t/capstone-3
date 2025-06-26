let categoryService;

class CategoryService {
    addCategory(category) {
        const url = `${config.baseUrl}/categories`;
        const headers = userService.getHeaders();

        axios.post(url, category, { headers })
            .then(response => {
                const data = {
                    message: "✅ Category added successfully."
                };
                templateBuilder.append("message", data, "errors");

                categoryService.getAllCategories(loadCategories);
            })
            .catch(error => {
                const data = {
                    error: "❌ Failed to add category. Make sure you are logged in as an admin."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    deleteCategory(id) {
        const url = `${config.baseUrl}/categories/${id}`;
        const headers = userService.getHeaders();

        axios.delete(url, { headers })
            .then(() => {
                const data = {
                    message: "🗑 Category deleted successfully."
                };
                templateBuilder.append("message", data, "errors");

                categoryService.getAllCategories(loadCategories);
            })
            .catch(() => {
                const data = {
                    error: "❌ Failed to delete category. Make sure you are logged in as an admin."
                };
                templateBuilder.append("error", data, "errors");
            });
    }

    getAllCategories(callback) {
        const url = `${config.baseUrl}/categories`;

        return axios.get(url)
            .then(response => {
                callback(response.data);
            })
            .catch(error => {
                const data = {
                    error: "Loading categories failed."
                };
                templateBuilder.append("error", data, "errors");
            });
    }
}

// ✅ Loads categories into select box and shows delete icons if admin
function loadCategories(categories) {
    const select = document.getElementById("category-select");
    if (select) {
        select.innerHTML = '<option value="0">Show All</option>';
        for (const category of categories) {
            const option = document.createElement("option");
            option.value = category.id;
            option.textContent = category.name;
            select.appendChild(option);
        }
    }

    // Optional: Show category list with 🗑 delete button
    const container = document.getElementById("category-list");
    if (container) {
        container.innerHTML = "";
        const isAdmin = userService.getCurrentUser().role === "ROLE_ADMIN";

        for (const category of categories) {
            const div = document.createElement("div");
            div.textContent = category.name;

            if (isAdmin) {
                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "🗑";
                deleteBtn.classList.add("btn", "btn-sm", "btn-danger");
                deleteBtn.style.marginLeft = "10px";
                deleteBtn.onclick = () => categoryService.deleteCategory(category.id);

                div.appendChild(deleteBtn);
            }

            container.appendChild(div);
        }
    }
}

// ✅ Add category button
function addNewCategory() {
    const name = document.getElementById("categoryName").value;
    const category = { name: name };
    categoryService.addCategory(category);
}

document.addEventListener('DOMContentLoaded', () => {
    categoryService = new CategoryService();
    categoryService.getAllCategories(loadCategories);
});
