"""
Natural Language Processing module.

This module will process English-language user input
and identify the user's intent.
"""

def detect_intent(user_input):
    """
    Detect the general intent of a user's legal question.

    Parameters:
        user_input (str): The user's question.

    Returns:
        str: Detected legal category.
    """

    text = user_input.lower()

    if "rent" in text or "tenant" in text or "landlord" in text:
        return "tenant_rights"

    if "job" in text or "employment" in text or "salary" in text:
        return "employment_dispute"

    if "land" in text or "property" in text or "ownership" in text:
        return "land_ownership"

    return "general_legal_information"
