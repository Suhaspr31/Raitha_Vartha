const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { GoogleGenerativeAI } = require("@google/generative-ai");
const pdfParse = require("pdf-parse");

admin.initializeApp();

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);

exports.processPdfUpload = functions.storage.object().onFinalize(async (object) => {
    const fileBucket = object.bucket;
    const filePath = object.name;
    const contentType = object.contentType;

    // Exit if this is triggered on a file that is not a PDF.
    if (!contentType.includes("pdf")) {
        return console.log("This is not a PDF.");
    }

    const bucket = admin.storage().bucket(fileBucket);
    const file = bucket.file(filePath);

    try {
        // 1. Download PDF file
        const [buffer] = await file.download();

        // 2. Parse PDF text
        const pdfData = await pdfParse(buffer);
        const text = pdfData.text;

        // 3. Send to Gemini for transformation
        const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });
        const prompt = `Extract 5 actionable tips from the following agricultural text. 
Translate to Kannada. Format as JSON with an array of objects containing {title, instruction, crop_type, image_search_keyword}. 
Do not include markdown tags like \`\`\`json.
Text: ${text.substring(0, 5000)}`;

        const result = await model.generateContent(prompt);
        const responseText = result.response.text();
        
        // 4. Parse JSON and store in Firestore
        const tipsArray = JSON.parse(responseText.trim());
        
        const batch = admin.firestore().batch();
        const tipsCollection = admin.firestore().collection("tips");

        tipsArray.forEach(tip => {
            const newDoc = tipsCollection.doc();
            batch.set(newDoc, {
                title: tip.title,
                descriptionKannada: tip.instruction,
                cropType: tip.crop_type,
                imageKeyword: tip.image_search_keyword,
                imageUrl: "", // Will be populated by a separate process or default
                timestamp: admin.firestore.FieldValue.serverTimestamp()
            });
        });

        await batch.commit();
        console.log("Successfully extracted and stored tips in Firestore.");

    } catch (error) {
        console.error("Error processing PDF:", error);
    }
});
