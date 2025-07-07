<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product List</title>
    <style>
        body {
            font-family: 'Avenir', sans-serif;
            margin: 0;
            padding: 20px;
            background: #f9f9f9;
        }

        h2 {
            font-size: 24px;
            margin-bottom: 20px;
            text-align: center;
            margin-top: 40px;
        }

        .product-scroll-container {
            display: flex;
            overflow-x: auto;
            gap: 40px;
            padding: 40px 20px 20px 20px;
            justify-content: flex-start;
            scroll-padding-left: 20px;
            scroll-padding-right: 40px;
            min-width: 100vw;
        }

        .product-card {
            flex: 0 0 auto;
            width: 250px;
            background: #fff;
            border-radius: 10px;
            padding: 15px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align: center;
        }

        .product-card img {
            width: 200px;
            height: auto;
            margin-bottom: 10px;
        }

        .product-name {
            font-family: 'Montserrat', sans-serif;
            font-size: 16px;
            font-weight: 500;
            margin: 5px 0;
        }

        .product-price {
            font-size: 14px;
            color: #555;
            margin-bottom: 5px;
        }

        .product-color {
            font-size: 13px;
            color: #777;
            margin-bottom: 10px;
        }

        .color-btn {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            display: inline-block;
            margin: 5px;
            cursor: pointer;
            border: 1px solid #ccc;
        }

        .stars {
            color: gold;
            font-size: 14px;
        }

        .product-card:last-child {
            margin-right: 40px;
        }

        .llm-section {
            background: #fff;
            border-radius: 10px;
            padding: 20px;
            margin: 20px auto;
            max-width: 600px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .llm-section h3 {
            color: #333;
            margin-bottom: 15px;
            text-align: center;
        }

        .llm-form {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
        }

        .llm-form input[type="text"] {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }

        .llm-form button {
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }

        .llm-form button:hover {
            background: #0056b3;
        }

        .llm-answer {
            background: #f8f9fa;
            border-left: 4px solid #007bff;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
        }

        .llm-question {
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }

        .llm-error {
            background: #f8d7da;
            border-left: 4px solid #dc3545;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
            color: #721c24;
        }
        @media (max-width: 1024px) {
            .product-scroll-container {
                gap: 24px;
                padding: 24px 10px 10px 10px;
            }
            .llm-section, .product-card {
                max-width: 95vw;
            }
        }
        @media (max-width: 768px) {
            h2 {
                font-size: 20px;
                margin-top: 24px;
            }
            .product-scroll-container {
                gap: 16px;
                padding: 16px 4vw 8px 4vw;
                min-width: 100vw;
            }
            .product-card {
                width: 90vw;
                min-width: 240px;
                max-width: 98vw;
                margin: 0 auto 16px auto;
                box-sizing: border-box;
            }
            .product-card img {
                width: 80vw;
                max-width: 220px;
            }
            .llm-section {
                padding: 12px;
                max-width: 98vw;
            }
            .llm-form, .llm-section form {
                flex-direction: column;
                gap: 8px;
            }
            .llm-form input[type="text"], .llm-section input, .llm-section button, .llm-section a {
                width: 100%;
                min-width: 0;
                box-sizing: border-box;
            }
            .slider-labels {
                font-size: 11px;
            }
        }
        @media (max-width: 480px) {
            h2 {
                font-size: 17px;
            }
            .product-card {
                width: 98vw;
                min-width: 180px;
                padding: 8px;
            }
            .product-card img {
                width: 90vw;
                max-width: 150px;
            }
            .llm-section {
                padding: 6px;
            }
        }
    </style>
</head>
<body>
<h2>Product List</h2>

<!-- Filtreleme Formu -->
<div class="llm-section" style="margin-bottom: 10px;">
    <form method="get" action="${pageContext.request.contextPath}/products" style="display: flex; flex-wrap: wrap; gap: 10px; align-items: center; justify-content: center;">
        <input type="number" step="0.01" name="minPrice" placeholder="Min Fiyat" value="${minPrice != null ? minPrice : ''}" style="width: 110px;">
        <input type="number" step="0.01" name="maxPrice" placeholder="Max Fiyat" value="${maxPrice != null ? maxPrice : ''}" style="width: 110px;">
        <input type="number" step="0.1" name="minPopularity" placeholder="Min Popülerlik" value="${minPopularity != null ? minPopularity : ''}" style="width: 130px;">
        <input type="number" step="0.1" name="maxPopularity" placeholder="Max Popülerlik" value="${maxPopularity != null ? maxPopularity : ''}" style="width: 130px;">
        <button type="submit" style="padding: 8px 18px; background: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer;">Filtrele</button>
        <a href="${pageContext.request.contextPath}/products" style="padding: 8px 18px; background: #6c757d; color: white; border: none; border-radius: 5px; text-decoration: none;">Temizle</a>
    </form>
</div>

<!-- LLM Soru-Cevap Alanı -->
<div class="llm-section">
    <h3>🤖 Akıllı Asistan</h3>
    <form method="post" action="${pageContext.request.contextPath}/ask-llm" class="llm-form">
        <input type="text" name="question" placeholder="Ürünler hakkında soru sorun... (Örn: En popüler yüzük hangisi?)" required>
        <button type="submit">Sor</button>
    </form>
    
    <c:if test="${not empty llmAnswer}">
        <div class="llm-answer">
            <div class="llm-question">Soru: ${llmQuestion}</div>
            <div>${llmAnswer}</div>
        </div>
    </c:if>
    
    <c:if test="${not empty llmError}">
        <div class="llm-error">
            ${llmError}
        </div>
    </c:if>
</div>

<div class="product-scroll-container">
    <c:forEach var="p" items="${products}">
        <div class="product-card">
            <img id="img-${p.name}" src="${p.images.yellow}" alt="${p.name}">
            <div class="product-name">${p.name}</div>
            <div class="product-price">$${p.price} USD</div>
            <div class="product-color">Yellow Gold</div>

            <div>
                <div class="color-btn" style="background: #E6CA97;"
                     onclick="document.getElementById('img-${p.name}').src='${p.images.yellow}'"></div>
                <div class="color-btn" style="background: #E1A4A9;"
                     onclick="document.getElementById('img-${p.name}').src='${p.images.rose}'"></div>
                <div class="color-btn" style="background: #D9D9D9;"
                     onclick="document.getElementById('img-${p.name}').src='${p.images.white}'"></div>
            </div>

            <div class="stars">
                <c:set var="rating" value="${p.popularityOutOf5}" />
                <c:forEach var="i" begin="1" end="5">
                    <c:choose>
                        <c:when test="${i <= rating}">
                            <!-- Dolu yıldız SVG -->
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="gold" style="vertical-align:middle;"><path d="M12 .587l3.668 7.568L24 9.423l-6 5.847L19.335 24 12 19.897 4.665 24 6 15.27 0 9.423l8.332-1.268z"/></svg>
                        </c:when>
                        <c:when test="${i - rating <= 0.5 && i - rating > 0}">
                            <!-- Yarım yıldız SVG -->
                            <svg width="16" height="16" viewBox="0 0 24 24" style="vertical-align:middle;">
                                <defs>
                                    <linearGradient id="half-grad">
                                        <stop offset="50%" stop-color="gold"/>
                                        <stop offset="50%" stop-color="lightgray"/>
                                    </linearGradient>
                                </defs>
                                <path fill="url(#half-grad)" d="M12 .587l3.668 7.568L24 9.423l-6 5.847L19.335 24 12 19.897 4.665 24 6 15.27 0 9.423l8.332-1.268z"/>
                            </svg>
                        </c:when>
                        <c:otherwise>
                            <!-- Boş yıldız SVG -->
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="lightgray" style="vertical-align:middle;"><path d="M12 .587l3.668 7.568L24 9.423l-6 5.847L19.335 24 12 19.897 4.665 24 6 15.27 0 9.423l8.332-1.268z"/></svg>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                (${p.popularityOutOf5} / 5)
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
