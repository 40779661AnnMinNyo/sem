FROM nginx:latest
LABEL authors="Ann Min Nyo"

# Copy your HTML and any assets
COPY content /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

