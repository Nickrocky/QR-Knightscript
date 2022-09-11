# QR-Knightscript
Shellhacks 2022 project

QR-Knightscript is a QR-Code project that effectively acts as a second revision to the CQR project made during another Hackathon. CQR had numerous issues regarding its data storage, lack of redundant encoding regions, no compression was applied to payloads, and there wasn't meaningful size constraints placed on the codes resulting in some QR Codes that could end up being larger than the paper they were intended for.

In addition Knightscript (A future-ish project) is a lightweight optimized simplistic scripting language that utilizes templates to front most of the code to the client to allow for in QR-Code applications that have all of their code within the glyph.

On a KQR there are 16 Packages (8 bytes) of information which encode our template number, which then can be hashed with sha256 to then be send to our repo server which would send the template to the user requiring only a one time download, then the user would NO LONGER need to have access to open the QRApp.

It is abundantly clear however, writing a new QR pattern, writer and reader was a bit out of scope for a 36 hour hackathon. It was amazing fun though.
